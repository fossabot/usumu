package io.usumu.api.subscription.controller;

import io.swagger.annotations.*;
import io.usumu.api.subscription.service.SubscriptionGetService;
import io.usumu.api.subscription.exception.SubscriptionNotFound;
import io.usumu.api.subscription.resource.SubscriptionResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import zone.refactor.spring.hateoas.annotation.EntityEndpoint;
import zone.refactor.spring.hateoas.contract.LinkProvider;

@RestController
@Api(
        tags = "Subscriptions"
)
@RequestMapping("/subscriptions")
public class SubscriptionGetApi {
    private final LinkProvider linkProvider;
    private final SubscriptionGetService subscriptionGetService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public SubscriptionGetApi(
            LinkProvider linkProvider,
            SubscriptionGetService subscriptionGetService
    ) {
        this.linkProvider = linkProvider;
        this.subscriptionGetService = subscriptionGetService;
    }

    @ApiOperation(
        nickname = "getSubscription",
        value = "Get a subscription",
        notes = "Get a subscription by providing the subscription entryType (EMAIL or SMS)." +
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
    @EntityEndpoint(SubscriptionResource.class)
    public SubscriptionResource get(
        @ApiParam(
            value = "Subscription ID, or subscriber contact info (EMAIL or PHONE in international format)",
            required = true
        )
        @PathVariable
            String value
    ) throws SubscriptionNotFound {
        return new SubscriptionResource(subscriptionGetService.get(value), linkProvider);
    }
}
