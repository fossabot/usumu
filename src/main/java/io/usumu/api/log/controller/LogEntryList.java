package io.usumu.api.log.controller;

import io.swagger.annotations.ApiModel;
import io.usumu.api.common.entity.PaginatedList;
import io.usumu.api.log.entity.LogEntry;
import io.usumu.api.log.resource.LogEntryResource;
import zone.refactor.spring.hateoas.contract.LinkProvider;

import java.util.List;
import java.util.stream.Collectors;

@ApiModel(
    description =
        "A paginated list of subscription log entries. The pagination is done via a continuation token that should be " +
            "submitted to the same endpoint to fetch the next page. When no continuation token is present you have " +
            "reached the last page."
)
public class LogEntryList extends PaginatedList<LogEntryResource> {
    public LogEntryList(final String continuationToken, final List<LogEntry> items, LinkProvider linkProvider) {
        super(
            continuationToken,
            items.stream().map(item -> new LogEntryResource(item, linkProvider)).collect(Collectors.toList())
        );
    }
}
