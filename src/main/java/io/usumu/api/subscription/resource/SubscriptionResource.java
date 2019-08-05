package io.usumu.api.subscription.resource;

import io.swagger.annotations.ApiModel;
import io.usumu.api.common.resource.LinkedResource;
import io.usumu.api.log.entity.SubscriptionLogEntry;
import io.usumu.api.subscription.entity.Subscription;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.lang.Nullable;

@ApiModel(
    "Subscription"
)
public class SubscriptionResource extends LinkedResource<SubscriptionResource.SubscriptionResourceLinks> {
    public final static String TYPE = "subscription";

    public final String id;
    public final Subscription.Type type;
    @Nullable
    public final String value;
    public final Subscription.Status status;

    public SubscriptionResource(Subscription subscription, EntityLinks entityLinks) {
        super(
            TYPE,
            new SubscriptionResourceLinks(
                entityLinks.linkFor(Subscription.class).withRel("up").withTitle("List of subscriptions"),
                entityLinks.linkFor(Subscription.class).slash(subscription.id).withSelfRel().withTitle("This subscription"),
                entityLinks.linkFor(SubscriptionLogEntry.class, subscription.id).withSelfRel().withTitle("The logs for this subscription")
            )
        );
        id = subscription.id;
        type = subscription.type;
        value = subscription.value;
        status = subscription.status;
    }

    @ApiModel("SubscriptionLinks")
    final static class SubscriptionResourceLinks {
        public final Link up;
        public final Link self;
        private final Link logs;

        SubscriptionResourceLinks(
            Link up,
            Link self,
            Link logs
        ) {
            this.up = up;
            this.self = self;
            this.logs = logs;
        }
    }
}
