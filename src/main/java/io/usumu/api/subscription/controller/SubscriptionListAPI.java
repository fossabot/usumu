package io.usumu.api.subscription.controller;

import io.swagger.annotations.*;
import io.usumu.api.subscription.entity.Subscription;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@RestController
@Api(
        tags = "Subscriptions"
)
public class SubscriptionListAPI {
    @ApiOperation(
            nickname = "listSubscriptions",
            value = "List subscriptions",
            notes = "List all subscriptions in the system, either confirmed or unconfirmed. The response is paginated.",
            consumes = "application/json",
            produces = "application/json"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "A list of current subscriptions is returned.", response = SubscriptionListAPI.SubscriptionListResponse.class),
            }
    )
    @RequestMapping(
            value = "/subscriptions",
            method = RequestMethod.GET
    )
    public SubscriptionListResponse list(
            @ApiParam(
                    value = "The number of items to return",
                    required = false
            )
            @RequestParam
            @Nullable
                    Integer itemCount,

            @ApiParam(
                    value = "Items to skip from the start",
                    required = false
            )
            @RequestParam
            @Nullable
                    Integer offset
    ) {
        return null;
    }

    public static class SubscriptionListResponse {
        @ApiParam(
                value = "The list of subscriptions"
        )
        public final List<Subscription> subscriptions;
        @ApiParam(
                value = "The total number of subscriptions"
        )
        public final int totalItems;

        public SubscriptionListResponse(List<Subscription> subscriptions, int totalItems) {
            this.subscriptions = subscriptions;
            this.totalItems = totalItems;
        }
    }
}
