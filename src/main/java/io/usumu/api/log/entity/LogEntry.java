package io.usumu.api.log.entity;

import javax.annotation.Nullable;
import java.time.ZonedDateTime;

public class LogEntry {
    public final String id;
    public final String subscriptionId;
    public final ZonedDateTime time;
    public final EntryType entryType;
    @Nullable
    public final String ipAddress;

    public LogEntry(
        String id,
        String subscriptionId,
        ZonedDateTime time,
        EntryType entryType,
        @Nullable
        String ipAddress
    ) {
        this.id = id;
        this.subscriptionId = subscriptionId;
        this.time = time;
        this.entryType = entryType;
        this.ipAddress = ipAddress;
    }

    public enum EntryType {
        IMPORTED_UNCONFIRMED,
        CREATED,
        RESENT_VERIFICATION,
        IMPORTED_CONFIRMED,
        VERIFIED,
        NEWSLETTER,
        IMPORTED_UNSUBSCRIBED,
        DELETED,
        RESUBSCRIBED;
    }
}
