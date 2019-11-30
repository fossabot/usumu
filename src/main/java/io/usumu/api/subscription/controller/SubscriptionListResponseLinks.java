package io.usumu.api.subscription.controller;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import org.springframework.lang.Nullable;
import zone.refactor.spring.hateoas.contract.Link;
import zone.refactor.spring.hateoas.contract.PartialLink;
import zone.refactor.spring.hateoas.entity.SelfUpLink;

@ApiModel("SubscriptionListLinks")
public class SubscriptionListResponseLinks extends SelfUpLink {
    @Nullable
    @ApiParam("Link to the next page of results.")
    public final Link next;

    public SubscriptionListResponseLinks(
        PartialLink self,
        PartialLink up,
        @Nullable
        Link next
    ) {
        super(
            self,
            up
        );
        this.next = next;
    }
}
