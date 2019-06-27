package io.usumu.api.log.controller;

import io.swagger.annotations.*;
import io.usumu.api.log.entity.SubscriptionLogEntry;
import io.usumu.api.subscription.controller.SubscriptionListAPI;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@RestController
@Api(
        tags = "Logs"
)
public class LogListApi {
    @ApiOperation(
            nickname = "listLogs",
            value = "List logs for subscriber",
            notes = "List all log entries related to a subscription. These logs are preserved even after deletion as legal proof.",
            consumes = "application/json",
            produces = "application/json"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "A list of all transactions related to a subscription.", response = SubscriptionLogListResponse.class),
            }
    )
    @RequestMapping(
            value = "/subscriptions/{value}/logs",
            method = RequestMethod.GET
    )
    public SubscriptionLogListResponse list(
            @ApiParam(
                    value = "Subscription ID, or subscriber contact info (EMAIL or PHONE in international format)",
                    required = true
            )
            @PathVariable
            String value
    ) {
            return null;
    }

    public static class SubscriptionLogListResponse {
        public final List<SubscriptionLogEntry> entries;

        public SubscriptionLogListResponse(List<SubscriptionLogEntry> entries) {
            this.entries = entries;
        }
    }
}
