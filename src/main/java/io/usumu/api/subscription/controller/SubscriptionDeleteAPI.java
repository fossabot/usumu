package io.usumu.api.subscription.controller;

import io.swagger.annotations.*;
import io.usumu.api.common.entity.ApiError;
import io.usumu.api.subscription.exception.SubscriptionNotFound;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.usumu.api.subscription.entity.Subscription;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@RestController
@Api(
        tags = "Subscriptions",
        produces = "application/json"
)
public class SubscriptionDeleteAPI {
    @ApiOperation(
            nickname = "deleteSubscription",
            value = "Delete a subscription",
            notes = "Delete a subscription by providing the subscription type (EMAIL or SMS)." +
                    "The value in this case is either the phone number in international format, or the e-mail address.",
            consumes = "application/json",
            produces = "application/json"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Successfully deleted the subscription. The subscription logs are still preserved for accountability.", response = SubscriptionDeleteResponse.class),
                    @ApiResponse(code = 404, message = "The subscription did not exist.", response = ApiError.class)
            }
    )
    @RequestMapping(
            value = "/subscriptions/{value}",
            method = RequestMethod.DELETE
    )
    public SubscriptionDeleteResponse delete(
            @ApiParam(
                    value = "Subscription ID, or subscriber contact info (EMAIL or PHONE in international format)",
                    required = true
            )
            @PathVariable
            String value
    ) throws SubscriptionNotFound {
        return null;
    }

    public static class SubscriptionDeleteResponse {
        public final Subscription subscription;

        public SubscriptionDeleteResponse(Subscription subscription) {
            this.subscription = subscription;
        }
    }
}
