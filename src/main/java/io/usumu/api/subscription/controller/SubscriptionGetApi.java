package io.usumu.api.subscription.controller;

import io.swagger.annotations.*;
import io.usumu.api.common.entity.ApiError;
import io.usumu.api.subscription.entity.Subscription;
import io.usumu.api.subscription.exception.SubscriptionNotFound;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@RestController
@Api(
        tags = "Subscriptions",
        produces = "application/json"
)
@ApiResponses(
        value = {
                @ApiResponse(code = 200, message = "Returns the subscription based on the e-mail address or phone number.", response = SubscriptionGetApi.SubscriptionGetResponse.class),
                @ApiResponse(code = 404, message = "The subscription was not found with the details in question.", response = ApiError.class)
        }
)
public class SubscriptionGetApi {
    @ApiOperation(
            nickname = "getSubscription",
            value = "Get a subscription",
            notes = "Get a subscription by providing the subscription type (EMAIL or SMS)." +
                    "The value in this case is either the phone number in international format, or the e-mail address.",
            consumes = "application/json",
            produces = "application/json"
    )
    @RequestMapping(
            value = "/subscriptions/{value}",
            method = RequestMethod.GET
    )
    public SubscriptionGetResponse get(
            @ApiParam(
                    value = "Subscription ID, or subscriber contact info (EMAIL or PHONE in international format)",
                    required = true
            )
            @PathVariable
                    String value
    ) throws SubscriptionNotFound {
        throw new SubscriptionNotFound();
    }

    public static class SubscriptionGetResponse {
        public final Subscription subscription;

        public SubscriptionGetResponse(Subscription subscription) {
            this.subscription = subscription;
        }
    }
}
