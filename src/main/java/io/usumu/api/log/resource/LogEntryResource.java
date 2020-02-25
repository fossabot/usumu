package io.usumu.api.log.resource;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.usumu.api.common.resource.SelfUpLink;
import io.usumu.api.log.entity.LogEntry;
import org.springframework.lang.Nullable;
import zone.refactor.spring.hateoas.contract.LinkProvider;
import zone.refactor.spring.hateoas.entity.LinkedEntity;

import java.time.ZonedDateTime;

@ApiModel("SubscriptionLogEntry")
public class LogEntryResource extends LinkedEntity<SelfUpLink> {
    public final String             id;
    public final String             subscriptionId;
    public final ZonedDateTime      time;
    public final LogEntry.EntryType entryType;
    @Nullable
    public final String             ipAddress;

    public LogEntryResource(
        LogEntry logEntry,
        LinkProvider linkProvider
    ) {
        super(new SelfUpLink(
            linkProvider.getResourceLink(LogEntryResource.class, logEntry.id, logEntry.subscriptionId),
            linkProvider.getResourceListLink(LogEntryResource.class, logEntry.subscriptionId)
        ));
        id = logEntry.id;
        subscriptionId = logEntry.subscriptionId;
        time = logEntry.time;
        entryType = logEntry.entryType;
        ipAddress = logEntry.ipAddress;
    }

    @JsonCreator
    public LogEntryResource(
        @JsonProperty(value = "id", required = true)
        final String id,
        @JsonProperty(value = "subscriptionId", required = true)
        final String subscriptionId,
        @JsonProperty(value = "time", required = true)
        final ZonedDateTime time,
        @JsonProperty(value = "entryType", required = true)
        final LogEntry.EntryType entryType,
        @Nullable
        @JsonProperty(value = "ipAddress", required = false)
        final String ipAddress,
        @JsonProperty(value = "_links", required = true)
        final SelfUpLink links
    ) {
        super(links);
        this.id = id;
        this.subscriptionId = subscriptionId;
        this.time = time;
        this.entryType = entryType;
        this.ipAddress = ipAddress;
    }
}
