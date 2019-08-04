package io.usumu.api.log.resource;

import io.usumu.api.common.resource.LinkedResource;
import io.usumu.api.log.entity.SubscriptionLogEntry;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;

public class SubscriptionLogEntryResource extends LinkedResource<SubscriptionLogEntryResource.SubscriptionLogEntryResourceLinks> {
    public final static String TYPE = "logEntry";

    public final String id;
    public final String subscriptionId;
    public final LocalDateTime time;
    public final SubscriptionLogEntry.Type type;
    public final String ipAddress;
    @Nullable
    public final String newsletterId;

    public SubscriptionLogEntryResource(
        SubscriptionLogEntry logEntry,
        EntityLinks entityLinks
    ) {
        super(TYPE, new SubscriptionLogEntryResourceLinks(
            entityLinks.linkFor(SubscriptionLogEntry.class, logEntry.subscriptionId).withRel("up").withTitle("All log entries for this subscription"),
            entityLinks.linkFor(SubscriptionLogEntry.class, logEntry.subscriptionId).slash(logEntry.id).withSelfRel().withTitle("This log entry")
        ));
        id = logEntry.id;
        subscriptionId = logEntry.subscriptionId;
        time = logEntry.time;
        type = logEntry.type;
        ipAddress = logEntry.ipAddress;
        newsletterId = logEntry.newsletterId;
    }

    public static class SubscriptionLogEntryResourceLinks {
        public final Link up;
        public final Link self;

        public SubscriptionLogEntryResourceLinks(
            Link up,
            Link self
        ) {
            this.up = up;
            this.self = self;
        }
    }
}
