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
import io.usumu.api.subscription.entity.EncryptedSubscription;
import io.usumu.api.subscription.entity.Subscription;
import io.usumu.api.subscription.exception.SubscriptionAlreadyExists;
import io.usumu.api.subscription.exception.SubscriptionNotFound;
import io.usumu.api.subscription.resource.SubscriptionResource;
import io.usumu.api.subscription.storage.SubscriptionStorageGet;
import io.usumu.api.subscription.storage.SubscriptionStorageUpsert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import zone.refactor.spring.hateoas.contract.LinkProvider;

@RestController
@Api(
        tags = "Subscriptions"
)
@RequestMapping("/subscriptions")
public class SubscriptionCreateApi {
    private final SubscriptionStorageGet subscriptionStorageGet;
    private final SubscriptionStorageUpsert subscriptionStorageUpsert;
    private final EntityCrypto entityCrypto;
    private final LinkProvider linkProvider;
    private final GlobalSecret globalSecret;
    private final SecretGenerator secretGenerator;
    private final HashGenerator hashGenerator;

    @Autowired
    public SubscriptionCreateApi(
        SubscriptionStorageGet subscriptionStorageGet,
        SubscriptionStorageUpsert subscriptionStorageUpsert,
        EntityCrypto entityCrypto,
        LinkProvider linkProvider,
        GlobalSecret globalSecret,
        SecretGenerator secretGenerator,
        HashGenerator hashGenerator
    ) {
        this.subscriptionStorageGet = subscriptionStorageGet;
        this.subscriptionStorageUpsert = subscriptionStorageUpsert;
        this.entityCrypto = entityCrypto;
        this.linkProvider = linkProvider;
        this.globalSecret = globalSecret;
        this.secretGenerator = secretGenerator;
        this.hashGenerator = hashGenerator;
    }

    @ApiOperation(
        nickname = "createSubscription",
        value = "Create a subscription",
        notes = "Create a subscription by providing the subscription type (EMAIL or SMS) and the contact details." +
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
                        message = "When the given value is invalid for the given type",
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
    ) throws SubscriptionAlreadyExists, InvalidParameters {
        //todo race condition possible
        Subscription subscription;
        EncryptedSubscription encryptedSubscription;
        try {
            encryptedSubscription = subscriptionStorageGet.get(request.value);
            //Conflict
            subscription = entityCrypto.decrypt(encryptedSubscription.encryptedData, Subscription.class);

            if (
                subscription.status != Subscription.Status.UNCONFIRMED &&
                subscription.status != Subscription.Status.UNSUBSCRIBED
            ) {
                //Already subscribed and confirmed
                throw new SubscriptionAlreadyExists();
            } else if (subscription.status == Subscription.Status.UNSUBSCRIBED) {
                //Resubscribe
                subscription = subscription.withSubscribeInitiated(request.value, globalSecret);
                encryptedSubscription = new EncryptedSubscription(
                    subscription, entityCrypto
                );
                subscriptionStorageUpsert.store(encryptedSubscription);
            }
        } catch (SubscriptionNotFound subscriptionNotFound) {
            //Create new subscription
            subscription = new Subscription(
                request.type,
                request.value,
                hashGenerator,
                secretGenerator
            );

            encryptedSubscription = new EncryptedSubscription(
                subscription,
                entityCrypto
            );

            subscriptionStorageUpsert.store(encryptedSubscription);
        }

        //todo notify user

        return new SubscriptionResource(
            subscription,
            hashGenerator,
            linkProvider
        );
    }
}
