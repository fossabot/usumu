package io.usumu.api.common.controller;

import io.usumu.api.subscription.resource.SubscriptionResource;
import zone.refactor.spring.hateoas.contract.Link;
import zone.refactor.spring.hateoas.contract.LinkProvider;
import zone.refactor.spring.hateoas.entity.LinkedEntity;
import zone.refactor.spring.hateoas.entity.SelfLink;

public class IndexResponse extends LinkedEntity<IndexResponse.IndexLinks> {
    public IndexResponse(
        LinkProvider linkProvider
    ) {
        super(new IndexLinks(linkProvider));
    }

    public static class IndexLinks extends SelfLink {
        public final Link subscriptions;

        public IndexLinks(LinkProvider linkProvider) {
            super(linkProvider.getResourceLink(IndexResponse.class));
            subscriptions = linkProvider.getResourceListLink(SubscriptionResource.class).withRel("subscriptions");
        }
    }
}
