package io.usumu.api.log.controller;

import io.swagger.annotations.*;
import io.usumu.api.log.resource.SubscriptionLogEntryResource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import zone.refactor.spring.hateoas.annotation.EntityEndpoint;

@RestController
@Api(
        tags = "Logs"
)
@RequestMapping("/subscriptions/{value}/logs")
public class LogGetApi {
    @ApiOperation(
            nickname = "getLogEntry",
            value = "Get a log entry",
            notes = "Get a single log entry for a subscription.",
            consumes = "application/json",
            produces = "application/json"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "A subscription log entry.", response = SubscriptionLogEntryResource.class),
            }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/{id}"
    )
    @EntityEndpoint(SubscriptionLogEntryResource.class)
    public SubscriptionLogEntryResource list(
            @ApiParam(
                    value = "Subscription ID, or subscriber contact info (EMAIL or PHONE in international format)",
                    required = true
            )
            @PathVariable
            String value,
            @ApiParam(
                value = "The log entry ID",
                required = true
            )
            @PathVariable
            String id
    ) {
            return null;
    }
}
