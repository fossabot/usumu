package io.usumu.api.log.resource;

import io.swagger.annotations.ApiModel;
import io.usumu.api.log.entity.LogEntry;
import org.springframework.lang.Nullable;
import zone.refactor.spring.hateoas.contract.LinkProvider;
import zone.refactor.spring.hateoas.entity.LinkedEntity;
import zone.refactor.spring.hateoas.entity.SelfUpLink;

import java.time.LocalDateTime;

@ApiModel("SubscriptionLogEntry")
public class SubscriptionLogEntryResource extends LinkedEntity<SelfUpLink> {
    public final String id;
    public final String subscriptionId;
    public final LocalDateTime time;
    public final LogEntry.EntryType entryType;
    public final String ipAddress;

    public SubscriptionLogEntryResource(
        LogEntry logEntry,
        LinkProvider linkProvider
    ) {
        super(new SelfUpLink(
            linkProvider.getResourceLink(SubscriptionLogEntryResource.class, logEntry.id, logEntry.subscriptionId),
            linkProvider.getResourceListLink(SubscriptionLogEntryResource.class, logEntry.subscriptionId)
        ));
        id = logEntry.id;
        subscriptionId = logEntry.subscriptionId;
        time = logEntry.time;
        entryType = logEntry.entryType;
        ipAddress = logEntry.ipAddress;
    }
}
