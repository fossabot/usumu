package io.usumu.api.subscription.resource;

import io.swagger.annotations.ApiModel;
import io.usumu.api.common.resource.LinkedResource;
import io.usumu.api.crypto.HashGenerator;
import io.usumu.api.log.entity.SubscriptionLogEntry;
import io.usumu.api.subscription.entity.Subscription;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.lang.Nullable;

@ApiModel(
    "Subscription"
)
public class SubscriptionResource extends LinkedResource<SubscriptionResource.SubscriptionResourceLinks> {
    @SuppressWarnings("WeakerAccess")
    public final static String TYPE = "subscription";

    @SuppressWarnings("WeakerAccess")
    public final String id;
    @SuppressWarnings("WeakerAccess")
    public final Subscription.Type type;
    @SuppressWarnings("WeakerAccess")
    @Nullable
    public final String value;
    @SuppressWarnings("WeakerAccess")
    @Nullable
    public final String verificationCode;
    @SuppressWarnings("WeakerAccess")
    public final Subscription.Status status;

    public SubscriptionResource(Subscription subscription, HashGenerator hashGenerator, EntityLinks entityLinks) {
        this(
            subscription,
            hashGenerator,
            entityLinks.linkFor(Subscription.class).withRel("up").withTitle("List of subscriptions"),
            entityLinks.linkFor(Subscription.class).slash(subscription.id).withSelfRel().withTitle("This subscription"),
            entityLinks.linkFor(SubscriptionLogEntry.class, subscription.id).withSelfRel().withTitle("The logs for this subscription")
        );
    }

    private SubscriptionResource(Subscription subscription, HashGenerator hashGenerator, Link up, Link self, Link logs) {
        super(
            TYPE,
            new SubscriptionResourceLinks(
                up,
                self,
                logs
            )
        );
        id = subscription.id;
        type = subscription.type;
        value = subscription.value;
        status = subscription.status;
        verificationCode = subscription.getVerificationCode(hashGenerator);
    }

    @SuppressWarnings("WeakerAccess")
    @ApiModel("SubscriptionLinks")
    public final static class SubscriptionResourceLinks {
        @SuppressWarnings({"WeakerAccess", "unused"})
        public final Link up;
        @SuppressWarnings({"WeakerAccess", "unused"})
        public final Link self;
        @SuppressWarnings({"WeakerAccess", "unused"})
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
