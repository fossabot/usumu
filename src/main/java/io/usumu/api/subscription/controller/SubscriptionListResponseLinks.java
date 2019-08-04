package io.usumu.api.subscription.controller;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import org.springframework.hateoas.Link;
import org.springframework.lang.Nullable;

@ApiModel("SubscriptionListLinks")
public class SubscriptionListResponseLinks {
    public final Link self;
    public final Link up;
    @Nullable
    @ApiParam("Link to the next page of results.")
    public final Link next;

    public SubscriptionListResponseLinks(
        Link self,
        Link up,
        @Nullable
        Link next
    ) {
        this.self = self;
        this.up = up;
        this.next = next;
    }
}
