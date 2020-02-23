package io.usumu.api.subscription.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.usumu.api.common.validation.InvalidParameters;
import io.usumu.api.crypto.EntityCrypto;
import io.usumu.api.crypto.GlobalSecret;
import io.usumu.api.crypto.HashGenerator;
import io.usumu.api.crypto.SecretGenerator;
import io.usumu.api.mail.MailSender;
import io.usumu.api.subscription.entity.EncryptedSubscription;
import io.usumu.api.subscription.entity.Subscription;
import io.usumu.api.subscription.entity.SubscriptionStatus;
import io.usumu.api.subscription.exception.SubscriptionAlreadyExists;
import io.usumu.api.subscription.exception.SubscriptionNotFound;
import io.usumu.api.subscription.resource.SubscriptionResource;
import io.usumu.api.subscription.service.SubscriptionGetService;
import io.usumu.api.subscription.service.SubscriptionUpdateService;
import io.usumu.api.subscription.storage.SubscriptionStorageGet;
import io.usumu.api.subscription.storage.SubscriptionStorageUpsert;
import io.usumu.api.template.TemplateProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final LinkProvider linkProvider;
    private final GlobalSecret globalSecret;
    private final SecretGenerator secretGenerator;
    private final HashGenerator hashGenerator;
    private final MailSender mailSender;
    private final SubscriptionGetService subscriptionGetService;
    private final SubscriptionUpdateService subscriptionUpdateService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public SubscriptionCreateApi(
            LinkProvider linkProvider,
            GlobalSecret globalSecret,
            SecretGenerator secretGenerator,
            HashGenerator hashGenerator,
            MailSender mailSender,
            SubscriptionGetService subscriptionGetService,
            SubscriptionUpdateService subscriptionUpdateService
    ) {
        this.linkProvider = linkProvider;
        this.globalSecret = globalSecret;
        this.secretGenerator = secretGenerator;
        this.hashGenerator = hashGenerator;
        this.mailSender = mailSender;
        this.subscriptionGetService = subscriptionGetService;
        this.subscriptionUpdateService = subscriptionUpdateService;
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
        Subscription subscription;
        EncryptedSubscription encryptedSubscription;
        try {
            //Conflict
            subscription = subscriptionGetService.get(request.value);

            if (
                    subscription.status != SubscriptionStatus.UNCONFIRMED &&
                    subscription.status != SubscriptionStatus.UNSUBSCRIBED
            ) {
                //Already subscribed and confirmed
                throw new SubscriptionAlreadyExists();
            } else if (subscription.status == SubscriptionStatus.UNSUBSCRIBED) {
                //Resubscribe
                subscription = subscription.withSubscribeInitiated(request.value, globalSecret);
            }
        } catch (SubscriptionNotFound subscriptionNotFound) {

            if (request.imported) {
                //Create new subscription w. status
                subscription = new Subscription(
                        request.method,
                        request.value,
                        request.importStatus,
                        hashGenerator,
                        secretGenerator
                );
            } else {
                //Create new subscription
                subscription = new Subscription(
                        request.method,
                        request.value,
                        hashGenerator,
                        secretGenerator
                );
            }
            subscriptionUpdateService.update(subscription);
        }

        if (!request.imported && subscription.status == SubscriptionStatus.UNCONFIRMED) {
            Map<String, Object> data = new HashMap<>();
            data.put("value", request.value);
            data.put("verificationCode", subscription.getVerificationCode(hashGenerator));
            data.put("subscription", subscription);

            mailSender.send(
                    "verification",
                    data
            );
        }

        return new SubscriptionResource(
                subscription,
                linkProvider
        );
    }
}
