package io.usumu.api.subscription.controller;

import io.swagger.annotations.*;
import io.usumu.api.log.entity.LogEntry;
import io.usumu.api.log.service.SubscriptionLogger;
import io.usumu.api.subscription.entity.Subscription;
import io.usumu.api.subscription.exception.SubscriptionAlreadyDeleted;
import io.usumu.api.subscription.exception.SubscriptionNotFound;
import io.usumu.api.subscription.resource.SubscriptionResource;
import io.usumu.api.subscription.service.SubscriptionDeleteService;
import io.usumu.api.subscription.service.SubscriptionGetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zone.refactor.spring.hateoas.contract.LinkProvider;

@RestController
@Api(
    tags = "Subscriptions"
)
@RequestMapping("/subscriptions")
public class SubscriptionDeleteApi {
    private final SubscriptionGetService    subscriptionGetService;
    private final LinkProvider              linkProvider;
    private final SubscriptionDeleteService subscriptionDeleteService;
    private final SubscriptionLogger        subscriptionLogger;

    @Autowired
    public SubscriptionDeleteApi(
        SubscriptionGetService subscriptionGetService,
        SubscriptionDeleteService subscriptionDeleteService,
        LinkProvider linkProvider,
        final SubscriptionLogger subscriptionLogger
    ) {
        this.subscriptionGetService = subscriptionGetService;
        this.subscriptionDeleteService = subscriptionDeleteService;
        this.linkProvider = linkProvider;
        this.subscriptionLogger = subscriptionLogger;
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
            String value,
        @RequestBody
            SubscriptionDeleteRequest request
    ) throws SubscriptionNotFound, SubscriptionAlreadyDeleted {
        Subscription subscription = subscriptionGetService.get(value);
        subscription = subscriptionDeleteService.delete(subscription);
        subscriptionLogger.log(subscription, LogEntry.EntryType.DELETED, request.remoteIp);

        return new SubscriptionResource(
            subscription,
            linkProvider
        );
    }
}
