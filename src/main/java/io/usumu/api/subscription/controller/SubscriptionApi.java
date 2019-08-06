package io.usumu.api.subscription.controller;

import io.swagger.annotations.*;
import io.usumu.api.common.entity.PaginatedList;
import io.usumu.api.common.validation.*;
import io.usumu.api.crypto.EntityCrypto;
import io.usumu.api.crypto.GlobalSecret;
import io.usumu.api.crypto.HashGenerator;
import io.usumu.api.crypto.SecretGenerator;
import io.usumu.api.subscription.entity.EncryptedSubscription;
import io.usumu.api.subscription.entity.Subscription;
import io.usumu.api.subscription.exception.*;
import io.usumu.api.subscription.resource.SubscriptionResource;
import io.usumu.api.subscription.storage.SubscriptionStorageGet;
import io.usumu.api.subscription.storage.SubscriptionStorageList;
import io.usumu.api.subscription.storage.SubscriptionStorageUpsert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Api(
        tags = "Subscriptions"
)
@RequestMapping("/subscriptions")
@ExposesResourceFor(Subscription.class)
public class SubscriptionApi {
    private final SubscriptionStorageList subscriptionStorageList;
    private final SubscriptionStorageGet subscriptionStorageGet;
    private final SubscriptionStorageUpsert subscriptionStorageUpsert;
    private final EntityCrypto entityCrypto;
    private final EntityLinks entityLinks;
    private final GlobalSecret globalSecret;
    private final SecretGenerator secretGenerator;
    private final HashGenerator hashGenerator;

    @Autowired
    public SubscriptionApi(
        SubscriptionStorageList subscriptionStorageList,
        SubscriptionStorageGet subscriptionStorageGet,
        SubscriptionStorageUpsert subscriptionStorageUpsert,
        EntityCrypto entityCrypto,
        EntityLinks entityLinks,
        GlobalSecret globalSecret,
        SecretGenerator secretGenerator,
        HashGenerator hashGenerator
    ) {
        this.subscriptionStorageList = subscriptionStorageList;
        this.subscriptionStorageGet = subscriptionStorageGet;
        this.subscriptionStorageUpsert = subscriptionStorageUpsert;
        this.entityCrypto = entityCrypto;
        this.entityLinks = entityLinks;
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
            entityLinks
        );
    }

    @ApiOperation(
        nickname = "getSubscription",
        value = "Get a subscription",
        notes = "Get a subscription by providing the subscription type (EMAIL or SMS)." +
            "The value in this case is either the phone number in international format, or the e-mail address.",
        consumes = "application/json",
        produces = "application/json"
    )
    @ApiResponses(
        value = {
            @ApiResponse(code = 200, message = "Returns the subscription based on the e-mail address or phone number.", response = SubscriptionResource.class),
            @ApiResponse(code = 404, message = "The subscription was not found with the details in question.", response = SubscriptionNotFound.class)
        }
    )
    @RequestMapping(
        value = "/{value}",
        method = RequestMethod.GET
    )
    public SubscriptionResource get(
        @ApiParam(
            value = "Subscription ID, or subscriber contact info (EMAIL or PHONE in international format)",
            required = true
        )
        @PathVariable
            String value
    ) throws SubscriptionNotFound {
        Subscription subscription = entityCrypto.decrypt(
            subscriptionStorageGet.get(value).encryptedData,
            Subscription.class
        );

        return new SubscriptionResource(subscription, hashGenerator, entityLinks);
    }

    @ApiOperation(
        nickname = "listSubscriptions",
        value = "List subscriptions",
        notes = "List all subscriptions in the system, either confirmed or unconfirmed. The response is paginated.",
        consumes = "application/json",
        produces = "application/json"
    )
    @ApiResponses(
        value = {
            @ApiResponse(code = 200, message = "A list of current subscriptions is returned.", response = SubscriptionListResponse.class),
        }
    )
    @RequestMapping(
        method = RequestMethod.GET
    )
    public SubscriptionListResponse list(
        @ApiParam(
            value = "The number of items to return"
        )
        @RequestParam(required = false)
        @Nullable
        Integer itemCount,

        @ApiParam(
            value = "Token to continue a listing"
        )
        @RequestParam(required = false, defaultValue = "")
        @Nullable
        String continuationToken
    ) throws InvalidParameters {
        ValidatorChain validatorChain = new ValidatorChain();

        validatorChain.addValidator("itemCount", new MinimumValidator(1));
        validatorChain.addValidator("itemCount", new MaximumValidator(100));
        validatorChain.addValidator("continuationToken", new SingleLineValidator());
        validatorChain.addValidator("continuationToken", new MaximumLengthValidator(255));

        Map<String, Object> data = new HashMap<>();
        data.put("itemCount", itemCount);
        data.put("continuationToken", continuationToken);
        validatorChain.validate(data);

        PaginatedList<EncryptedSubscription> encryptedSubscriptions = subscriptionStorageList
            .list(itemCount == null ? 100 : itemCount, continuationToken);

        List<Subscription> subscriptions = encryptedSubscriptions
            .items
            .stream()
            .map(encryptedSubscription -> {
                try {
                    return entityCrypto.decrypt(encryptedSubscription.encryptedData, Subscription.class);
                } catch (DecryptionFailed decryptionFailed) {
                    throw new RuntimeException(decryptionFailed);
                }
            })
            .collect(Collectors.toList());

        return new SubscriptionListResponse(
            subscriptions,
            encryptedSubscriptions.continuationToken,
            hashGenerator,
            entityLinks
        );
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
        EncryptedSubscription encryptedSubscription = subscriptionStorageGet.get(value);
        Subscription subscription = entityCrypto.decrypt(encryptedSubscription.encryptedData, Subscription.class);
        subscription = subscription.verify(
            hashGenerator,
            secretGenerator,
            request.verificationCode
        );
        subscriptionStorageUpsert.store(new EncryptedSubscription(subscription, entityCrypto));

        return new SubscriptionResource(
            subscription,
            hashGenerator,
            entityLinks
        );
    }

    @ApiOperation(
        nickname = "deleteSubscription",
        value = "Delete a subscription",
        notes = "Delete a subscription by providing the subscription type (EMAIL or SMS)." +
            "The value in this case is either the phone number in international format, or the e-mail address.",
        consumes = "application/json",
        produces = "application/json"
    )
    @ApiResponses({
        @ApiResponse(code = 200, message = "Deletion successful.", response = SubscriptionResource.class),
        @ApiResponse(code = 404, message = "The subscription with the e-mail or phone number does not exist.", response = SubscriptionNotFound.class),
        @ApiResponse(code = 410, message = "The subscription was already deleted.", response = SubscriptionAlreadyDeleted.class),
    })
    @RequestMapping(
        value = "/{value}",
        method = RequestMethod.DELETE,
        consumes = "application/json"
    )
    public SubscriptionResource delete(
        @ApiParam(
            value = "Subscriber ID, or subscriber contact info (EMAIL or PHONE in international format)",
            required = true
        )
        @PathVariable
        String value
    ) throws SubscriptionNotFound, SubscriptionAlreadyDeleted {
        EncryptedSubscription encryptedSubscription = subscriptionStorageGet.get(value);
        Subscription subscription = entityCrypto.decrypt(encryptedSubscription.encryptedData, Subscription.class);
        if (subscription.status.equals(Subscription.Status.UNSUBSCRIBED)) {
            throw new SubscriptionAlreadyDeleted();
        }
        subscription = subscription.withUnsubscribed();
        subscriptionStorageUpsert.store(new EncryptedSubscription(subscription, entityCrypto));

        return new SubscriptionResource(
            subscription,
            hashGenerator,
            entityLinks
        );
    }

    public static class SubscriptionVerifyResponse {
        public final SubscriptionResource subscription;

        public SubscriptionVerifyResponse(SubscriptionResource subscription) {
            this.subscription = subscription;
        }
    }
}
