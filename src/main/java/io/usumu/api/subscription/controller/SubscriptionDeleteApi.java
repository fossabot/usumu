package io.usumu.api.subscription.controller;

import io.swagger.annotations.*;
import io.usumu.api.crypto.EntityCrypto;
import io.usumu.api.crypto.HashGenerator;
import io.usumu.api.subscription.entity.EncryptedSubscription;
import io.usumu.api.subscription.entity.Subscription;
import io.usumu.api.subscription.entity.SubscriptionStatus;
import io.usumu.api.subscription.exception.SubscriptionAlreadyDeleted;
import io.usumu.api.subscription.exception.SubscriptionNotFound;
import io.usumu.api.subscription.resource.SubscriptionResource;
import io.usumu.api.subscription.storage.SubscriptionStorageGet;
import io.usumu.api.subscription.storage.SubscriptionStorageUpsert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import zone.refactor.spring.hateoas.contract.LinkProvider;

@RestController
@Api(
        tags = "Subscriptions"
)
@RequestMapping("/subscriptions")
public class SubscriptionDeleteApi {
    private final SubscriptionStorageGet subscriptionStorageGet;
    private final SubscriptionStorageUpsert subscriptionStorageUpsert;
    private final EntityCrypto entityCrypto;
    private final LinkProvider linkProvider;
    private final HashGenerator hashGenerator;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public SubscriptionDeleteApi(
        SubscriptionStorageGet subscriptionStorageGet,
        SubscriptionStorageUpsert subscriptionStorageUpsert,
        EntityCrypto entityCrypto,
        LinkProvider linkProvider,
        HashGenerator hashGenerator
    ) {
        this.subscriptionStorageGet = subscriptionStorageGet;
        this.subscriptionStorageUpsert = subscriptionStorageUpsert;
        this.entityCrypto = entityCrypto;
        this.linkProvider = linkProvider;
        this.hashGenerator = hashGenerator;
    }

    @ApiOperation(
        nickname = "deleteSubscription",
        value = "Delete a subscription",
        notes = "Delete a subscription by providing the subscription entryType (EMAIL or SMS)." +
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
        if (subscription.status.equals(SubscriptionStatus.UNSUBSCRIBED)) {
            throw new SubscriptionAlreadyDeleted();
        }
        subscription = subscription.withUnsubscribed();
        subscriptionStorageUpsert.store(new EncryptedSubscription(subscription, entityCrypto));

        return new SubscriptionResource(
            subscription,
            linkProvider
        );
    }

}
