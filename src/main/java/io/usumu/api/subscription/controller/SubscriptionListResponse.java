package io.usumu.api.subscription.controller;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import io.usumu.api.common.controller.IndexResponse;
import io.usumu.api.common.resource.LinkedResource;
import io.usumu.api.subscription.entity.Subscription;
import io.usumu.api.subscription.resource.SubscriptionResource;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.lang.Nullable;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

@ApiModel("SubscriptionList")
public class SubscriptionListResponse extends LinkedResource<SubscriptionListResponseLinks> {
    public final static String TYPE = "subscriptionList";

    @ApiParam(
        value = "The list of subscriptions",
        required = true
    )
    public final List<SubscriptionResource> subscriptions;

    @Nullable
    @ApiParam(
        value = "This token can be used to continue a listing.",
        required = false
    )
    public final String continuationToken;

    @Nullable
    private static Link getNextLink(EntityLinks entityLinks, @Nullable String continuationToken) {
        if (continuationToken == null) {
            return null;
        }
        Link next = entityLinks.linkFor(Subscription.class).withRel("next");
        try {
            return next.withHref(next.getHref() + "continuationToken=" + URLEncoder.encode(continuationToken, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public SubscriptionListResponse(List<Subscription> subscriptions, @Nullable String continuationToken, EntityLinks entityLinks) {
        super(TYPE, new SubscriptionListResponseLinks(
            entityLinks.linkFor(Subscription.class).withSelfRel(),
            entityLinks.linkFor(IndexResponse.class).withRel("up"),
            getNextLink(entityLinks, continuationToken)
        ));
        this.subscriptions = subscriptions.stream().map(subscription -> new SubscriptionResource(
            subscription, entityLinks
        )).collect(Collectors.toList());
        this.continuationToken = continuationToken;
    }
}
