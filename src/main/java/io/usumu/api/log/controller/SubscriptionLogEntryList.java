package io.usumu.api.log.controller;

import io.swagger.annotations.ApiModel;
import io.usumu.api.common.entity.PaginatedList;
import io.usumu.api.log.resource.SubscriptionLogEntryResource;

import java.util.List;

@ApiModel(
    description =
        "A paginated list of subscription log entries. The pagination is done via a continuation token that should be " +
            "submitted to the same endpoint to fetch the next page. When no continuation token is present you have " +
            "reached the last page."
)
public class SubscriptionLogEntryList extends PaginatedList<SubscriptionLogEntryResource> {
    public SubscriptionLogEntryList(final String continuationToken, final List<SubscriptionLogEntryResource> items) {
        super(continuationToken, items);
    }
}
