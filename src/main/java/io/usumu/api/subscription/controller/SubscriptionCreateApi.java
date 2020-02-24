package io.usumu.api.subscription.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.usumu.api.common.validation.InvalidParameters;
import io.usumu.api.crypto.GlobalSecret;
import io.usumu.api.crypto.HashGenerator;
import io.usumu.api.crypto.SecretGenerator;
import io.usumu.api.log.entity.LogEntry;
import io.usumu.api.log.service.SubscriptionLogger;
import io.usumu.api.mail.MailSender;
import io.usumu.api.subscription.entity.EncryptedSubscription;
import io.usumu.api.subscription.entity.Subscription;
import io.usumu.api.subscription.entity.SubscriptionStatus;
import io.usumu.api.subscription.exception.SubscriptionAlreadyExists;
import io.usumu.api.subscription.exception.SubscriptionNotFound;
import io.usumu.api.subscription.resource.SubscriptionResource;
import io.usumu.api.subscription.service.SubscriptionGetService;
import io.usumu.api.subscription.service.SubscriptionUpdateService;
import io.usumu.api.template.TemplateProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import zone.refactor.spring.hateoas.contract.LinkProvider;

import java.util.HashMap;
import java.util.Map;

@RestController
@Api(
    tags = "Subscriptions"
)
@RequestMapping("/subscriptions")
public class SubscriptionCreateApi {
    private final LinkProvider              linkProvider;
    private final GlobalSecret              globalSecret;
    private final SecretGenerator           secretGenerator;
    private final HashGenerator             hashGenerator;
    private final MailSender                mailSender;
    private final SubscriptionGetService    subscriptionGetService;
    private final SubscriptionUpdateService subscriptionUpdateService;
    private final SubscriptionLogger        subscriptionLogger;

    @Autowired
    public SubscriptionCreateApi(
        final LinkProvider linkProvider,
        final GlobalSecret globalSecret,
        final SecretGenerator secretGenerator,
        final HashGenerator hashGenerator,
        final MailSender mailSender,
        final SubscriptionGetService subscriptionGetService,
        final SubscriptionUpdateService subscriptionUpdateService,
        final SubscriptionLogger subscriptionLogger
    ) {
        this.linkProvider = linkProvider;
        this.globalSecret = globalSecret;
        this.secretGenerator = secretGenerator;
        this.hashGenerator = hashGenerator;
        this.mailSender = mailSender;
        this.subscriptionGetService = subscriptionGetService;
        this.subscriptionUpdateService = subscriptionUpdateService;
        this.subscriptionLogger = subscriptionLogger;
    }

    @ApiOperation(
        nickname = "createSubscription",
        value = "Create a subscription",
        notes = "Create a subscription by providing the subscription entryType (EMAIL or SMS) and the contact details." +
                "The value in this case is either the phone number in international format (+123456789), or the e-mail address." +
                "When the subscription is created a confirmation message is sent to the user to confirm their" +
                "subscription.",
        consumes = "application/json",
        produces = "application/json"
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                code = 200,
                message = "Created the subscription",
                response = SubscriptionResource.class
            ),
            @ApiResponse(
                code = 400,
                message = "When the given value is invalid for the given entryType",
                response = InvalidParameters.class
            ),
            @ApiResponse(
                code = 409,
                message = "When the given subscription already exists",
                response = SubscriptionAlreadyExists.class
            )
        }
    )
    @RequestMapping(
        method = RequestMethod.POST,
        consumes = "application/json",
        produces = "application/json"
    )
    public SubscriptionResource create(
        @RequestBody
            SubscriptionCreateRequest request
    ) throws SubscriptionAlreadyExists, InvalidParameters, TemplateProvider.TemplateNotFound {
        //todo race condition possible
        Subscription          subscription;
        EncryptedSubscription encryptedSubscription;
        try {
            //Conflict
            subscription = subscriptionGetService.get(request.value);
            switch (subscription.status) {
                case UNCONFIRMED:
                    //Re-send verification email
                    subscriptionLogger.log(subscription, LogEntry.EntryType.RESENT_VERIFICATION, request.remoteIp);
                    break;
                case CONFIRMED:
                    throw new SubscriptionAlreadyExists();
                case UNSUBSCRIBED:
                    //Resubscribe
                    subscriptionLogger.log(subscription, LogEntry.EntryType.RESUBSCRIBED, request.remoteIp);
                    subscription = subscription.withSubscribeInitiated(request.value, globalSecret);
                    break;
            }
        } catch (SubscriptionNotFound subscriptionNotFound) {
            if (request.imported) {
                SubscriptionStatus importStatus;
                if (request.importStatus == null) {
                    importStatus = SubscriptionStatus.UNCONFIRMED;
                } else {
                    importStatus = request.importStatus;
                }
                //Create new subscription w. status
                subscription = new Subscription(
                    request.method,
                    request.value,
                    importStatus,
                    hashGenerator,
                    secretGenerator
                );
                switch (importStatus) {
                    case UNCONFIRMED:
                        subscriptionLogger.log(subscription, LogEntry.EntryType.IMPORTED_UNCONFIRMED, request.remoteIp);
                        break;
                    case CONFIRMED:
                        subscriptionLogger.log(subscription, LogEntry.EntryType.IMPORTED_CONFIRMED, request.remoteIp);
                        break;
                    case UNSUBSCRIBED:
                        subscriptionLogger.log(
                            subscription,
                            LogEntry.EntryType.IMPORTED_UNSUBSCRIBED,
                            request.remoteIp
                        );
                        break;
                }
            } else {
                //Create new subscription
                subscription = new Subscription(
                    request.method,
                    request.value,
                    hashGenerator,
                    secretGenerator
                );
                subscriptionLogger.log(subscription, LogEntry.EntryType.CREATED, request.remoteIp);
            }
            subscriptionUpdateService.update(subscription);
        }

        if (!request.imported) {
            if (subscription.status == SubscriptionStatus.UNCONFIRMED) {
                Map<String, Object> data = new HashMap<>();
                data.put("value", request.value);
                data.put("verificationCode", subscription.getVerificationCode(hashGenerator));
                data.put("subscription", subscription);

                mailSender.send(
                    "verification",
                    data
                );
            }
        }

        return new SubscriptionResource(
            subscription,
            linkProvider
        );
    }
}
