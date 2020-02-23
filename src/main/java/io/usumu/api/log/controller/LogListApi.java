package io.usumu.api.log.controller;

import io.swagger.annotations.*;
import io.usumu.api.common.entity.PaginatedList;
import io.usumu.api.log.entity.LogEntry;
import io.usumu.api.log.resource.LogEntryResource;
import io.usumu.api.log.service.LogEntryListService;
import io.usumu.api.subscription.entity.Subscription;
import io.usumu.api.subscription.exception.SubscriptionNotFound;
import io.usumu.api.subscription.service.SubscriptionGetService;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import zone.refactor.spring.hateoas.annotation.ListingEndpoint;
import zone.refactor.spring.hateoas.contract.LinkProvider;

@RestController
@Api(
        tags = "Logs"
)
@RequestMapping("/subscriptions/{value}/logs")
public class LogListApi {
    private final SubscriptionGetService subscriptionGetService;
    private final LogEntryListService logEntryListService;
    private final LinkProvider linkProvider;

    public LogListApi(
        SubscriptionGetService subscriptionGetService,
        LogEntryListService logEntryListService,
        LinkProvider linkProvider
    ) {
        this.subscriptionGetService = subscriptionGetService;
        this.logEntryListService = logEntryListService;
        this.linkProvider = linkProvider;
    }

    @ApiOperation(
            nickname = "listLogs",
            value = "List logs for subscriber",
            notes = "List all log entries related to a subscription. These logs are preserved even after deletion as legal proof.",
            consumes = "application/json",
            produces = "application/json"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                        code = 200,
                        message = "A list of all transactions related to a subscription.",
                        response = LogEntryList.class
                    ),
            }
    )
    @RequestMapping(
            method = RequestMethod.GET
    )
    @ListingEndpoint(LogEntryResource.class)
    public LogEntryList list(
        @ApiParam(
                    value = "Subscription ID, or subscriber contact info (EMAIL or PHONE in international format)",
                    required = true
            )
            @PathVariable(value = "value")
            String value,
            @SuppressWarnings("DefaultAnnotationParam")
            @Nullable
            @ApiParam(
                value = "Number of items to return",
                required = false,
                allowableValues = "range(0,100)"
            )
            Integer itemCount,
            @SuppressWarnings("DefaultAnnotationParam")
            @Nullable
            @ApiParam(
                value = "Continuation token for next page of results.",
                required = false,
                allowEmptyValue = true
            )
            String continuationToken
    ) throws SubscriptionNotFound {
        Subscription subscription = subscriptionGetService.get(value);
        PaginatedList<LogEntry> result = logEntryListService.list(subscription, itemCount, continuationToken);

        return new LogEntryList(
            result.continuationToken,
            result.items,
            linkProvider
        );
    }
}
