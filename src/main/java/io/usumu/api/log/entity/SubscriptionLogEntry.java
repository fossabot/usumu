package io.usumu.api.log.entity;

import org.springframework.lang.Nullable;

import java.time.LocalDateTime;

public class SubscriptionLogEntry {
    public final String id;
    public final String subscriptionId;
    public final LocalDateTime time;
    public final EntryType entryType;
    public final String ipAddress;
    @Nullable
    public final String newsletterId;


    public SubscriptionLogEntry(
        String id,
        String subscriptionId,
        LocalDateTime time,
        EntryType entryType,
        String ipAddress,
        @Nullable String newsletterId
    ) {
        this.id = id;
        this.subscriptionId = subscriptionId;
        this.time = time;
        this.entryType = entryType;
        this.ipAddress = ipAddress;
        this.newsletterId = newsletterId;
    }

    public enum EntryType {
        IMPORTED_CREATED,
        CREATED,
        IMPORTED_VERIFIED,
        VERIFIED,
        NEWSLETTER,
        IMPORTED_DELETED,
        DELETED
    }
}
