package io.usumu.api.subscription.controller;

import io.swagger.annotations.*;
import io.usumu.api.crypto.EntityCrypto;
import io.usumu.api.crypto.HashGenerator;
import io.usumu.api.subscription.entity.Subscription;
import io.usumu.api.subscription.exception.SubscriptionNotFound;
import io.usumu.api.subscription.resource.SubscriptionResource;
import io.usumu.api.subscription.storage.SubscriptionStorageGet;
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
public class SubscriptionGetApi {
    private final SubscriptionStorageGet subscriptionStorageGet;
    private final EntityCrypto entityCrypto;
    private final LinkProvider linkProvider;
    private final HashGenerator hashGenerator;

    @Autowired
    public SubscriptionGetApi(
        SubscriptionStorageGet subscriptionStorageGet,
        EntityCrypto entityCrypto,
        LinkProvider linkProvider,
        HashGenerator hashGenerator
    ) {
        this.subscriptionStorageGet = subscriptionStorageGet;
        this.entityCrypto = entityCrypto;
        this.linkProvider = linkProvider;
        this.hashGenerator = hashGenerator;
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

        return new SubscriptionResource(subscription, hashGenerator, linkProvider);
    }
}