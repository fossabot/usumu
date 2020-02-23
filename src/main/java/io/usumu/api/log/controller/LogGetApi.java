package io.usumu.api.log.controller;

import io.swagger.annotations.*;
import io.usumu.api.log.exception.LogEntryNotFound;
import io.usumu.api.log.resource.LogEntryResource;
import io.usumu.api.log.service.LogEntryGetService;
import io.usumu.api.subscription.entity.Subscription;
import io.usumu.api.subscription.exception.SubscriptionNotFound;
import io.usumu.api.subscription.service.SubscriptionGetService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import zone.refactor.spring.hateoas.annotation.EntityEndpoint;
import zone.refactor.spring.hateoas.contract.LinkProvider;

@RestController
@Api(
        tags = "Logs"
)
@RequestMapping("/subscriptions/{value}/logs")
public class LogGetApi {
    private final SubscriptionGetService subscriptionGetService;
    private final LogEntryGetService logEntryGetService;
    private final LinkProvider linkProvider;

    public LogGetApi(SubscriptionGetService subscriptionGetService, LogEntryGetService logEntryGetService, LinkProvider linkProvider) {
        this.subscriptionGetService = subscriptionGetService;
        this.logEntryGetService = logEntryGetService;
        this.linkProvider = linkProvider;
    }

    @ApiOperation(
            nickname = "getLogEntry",
            value = "Get a log entry",
            notes = "Get a single log entry for a subscription.",
            consumes = "application/json",
            produces = "application/json"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "A subscription log entry.", response = LogEntryResource.class),
            }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/{id}"
    )
    @EntityEndpoint(LogEntryResource.class)
    public LogEntryResource list(
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
    ) throws SubscriptionNotFound, LogEntryNotFound {
        Subscription subscription = subscriptionGetService.get(value);
        return new LogEntryResource(logEntryGetService.get(subscription, id), linkProvider);
    }
}
