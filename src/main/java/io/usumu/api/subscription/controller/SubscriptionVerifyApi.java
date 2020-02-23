package io.usumu.api.subscription.controller;

import io.swagger.annotations.*;
import io.usumu.api.crypto.EntityCrypto;
import io.usumu.api.crypto.HashGenerator;
import io.usumu.api.crypto.SecretGenerator;
import io.usumu.api.subscription.service.SubscriptionGetService;
import io.usumu.api.subscription.entity.EncryptedSubscription;
import io.usumu.api.subscription.entity.Subscription;
import io.usumu.api.subscription.exception.SubscriptionNotFound;
import io.usumu.api.subscription.exception.VerificationFailed;
import io.usumu.api.subscription.resource.SubscriptionResource;
import io.usumu.api.subscription.service.SubscriptionUpdateService;
import io.usumu.api.subscription.storage.SubscriptionStorageGet;
import io.usumu.api.subscription.storage.SubscriptionStorageUpsert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zone.refactor.spring.hateoas.contract.LinkProvider;

@RestController
@Api(
        tags = "Subscriptions"
)
@RequestMapping("/subscriptions")
public class SubscriptionVerifyApi {
    private final SubscriptionUpdateService subscriptionUpdateService;
    private final LinkProvider linkProvider;
    private final SecretGenerator secretGenerator;
    private final HashGenerator hashGenerator;
    private final SubscriptionGetService subscriptionGetService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public SubscriptionVerifyApi(
            SubscriptionUpdateService subscriptionUpdateService,
            LinkProvider linkProvider,
            SecretGenerator secretGenerator,
            HashGenerator hashGenerator,
            SubscriptionGetService subscriptionGetService
    ) {
        this.subscriptionUpdateService = subscriptionUpdateService;
        this.linkProvider = linkProvider;
        this.secretGenerator = secretGenerator;
        this.hashGenerator = hashGenerator;
        this.subscriptionGetService = subscriptionGetService;
    }

    @ApiResponses({
        @ApiResponse(code = 200, message = "Verification successful.", response = SubscriptionVerifyResponse.class),
        @ApiResponse(code = 400, message = "The verification code submitted is invalid, verification failed.", response = VerificationFailed.class),
        @ApiResponse(code = 404, message = "The subscription with the e-mail or phone number does not exist.", response = SubscriptionNotFound.class),
    })
    @ApiOperation(
        nickname = "verifySubscription",
        value = "Verify a subscription",
        notes = "Verify a subscription by providing a verification code.",
        consumes = "application/json",
        produces = "application/json"
    )
    @RequestMapping(
        value = "/{value}",
        method = RequestMethod.PATCH,
        consumes = "application/json"
    )
    public SubscriptionResource verify(
        @ApiParam(
            value = "Subscriber ID, or subscriber contact info (EMAIL or PHONE in international format)",
            required = true
        )
        @PathVariable
        String value,
        @RequestBody
        SubscriptionVerifyRequest request
    ) throws SubscriptionNotFound, VerificationFailed {
        Subscription subscription = subscriptionGetService.get(value);

        subscription = subscription.verify(
            hashGenerator,
            secretGenerator,
            request.verificationCode
        );
        subscriptionUpdateService.update(subscription);

        return new SubscriptionResource(
            subscription,
            linkProvider
        );
    }
}
