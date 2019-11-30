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
import io.usumu.api.subscription.exception.DecryptionFailed;
import io.usumu.api.subscription.exception.SubscriptionNotFound;
import io.usumu.api.subscription.exception.VerificationFailed;
import io.usumu.api.subscription.resource.SubscriptionResource;
import io.usumu.api.subscription.storage.SubscriptionStorageGet;
import io.usumu.api.subscription.storage.SubscriptionStorageList;
import io.usumu.api.subscription.storage.SubscriptionStorageUpsert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import zone.refactor.spring.hateoas.contract.LinkProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Api(
        tags = "Subscriptions"
)
@RequestMapping("/subscriptions")
public class SubscriptionVerifyApi {
    private final SubscriptionStorageList subscriptionStorageList;
    private final SubscriptionStorageGet subscriptionStorageGet;
    private final SubscriptionStorageUpsert subscriptionStorageUpsert;
    private final EntityCrypto entityCrypto;
    private final LinkProvider linkProvider;
    private final GlobalSecret globalSecret;
    private final SecretGenerator secretGenerator;
    private final HashGenerator hashGenerator;

    @Autowired
    public SubscriptionVerifyApi(
        SubscriptionStorageList subscriptionStorageList,
        SubscriptionStorageGet subscriptionStorageGet,
        SubscriptionStorageUpsert subscriptionStorageUpsert,
        EntityCrypto entityCrypto,
        LinkProvider linkProvider,
        GlobalSecret globalSecret,
        SecretGenerator secretGenerator,
        HashGenerator hashGenerator
    ) {
        this.subscriptionStorageList = subscriptionStorageList;
        this.subscriptionStorageGet = subscriptionStorageGet;
        this.subscriptionStorageUpsert = subscriptionStorageUpsert;
        this.entityCrypto = entityCrypto;
        this.linkProvider = linkProvider;
        this.globalSecret = globalSecret;
        this.secretGenerator = secretGenerator;
        this.hashGenerator = hashGenerator;
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
            linkProvider
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
            linkProvider
        );
    }
}
