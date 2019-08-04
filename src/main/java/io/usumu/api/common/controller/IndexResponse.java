package io.usumu.api.common.controller;

import io.usumu.api.common.resource.LinkedResource;
import io.usumu.api.subscription.entity.Subscription;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;

public class IndexResponse extends LinkedResource<IndexResponse.IndexLinks> {
    public final static String TYPE = "index";

    public IndexResponse(
        EntityLinks entityLinks
    ) {
        super(TYPE, new IndexLinks(entityLinks));
    }

    public static class IndexLinks {
        public final Link self;
        public final Link swaggerUi;
        public final Link api;
        public final Link subscriptions;

        public IndexLinks(EntityLinks entityLinks) {
            self = entityLinks.linkFor(IndexResponse.class).withSelfRel();
            swaggerUi = entityLinks.linkFor(IndexResponse.class).slash("swagger-ui.html").withRel("swaggerUi").withType("text/html");
            api = entityLinks.linkFor(IndexResponse.class).slash("api.json").withRel("api").withType("application/openapi+json");
            subscriptions = entityLinks.linkFor(Subscription.class).withRel("subscriptions");
        }
    }
}
