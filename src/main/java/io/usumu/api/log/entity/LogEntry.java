package io.usumu.api.log.entity;

import org.springframework.lang.Nullable;

import java.time.LocalDateTime;

public class LogEntry {
    public final String id;
    public final String subscriptionId;
    public final LocalDateTime time;
    public final EntryType entryType;
    public final String ipAddress;

    public LogEntry(
        String id,
        String subscriptionId,
        LocalDateTime time,
        EntryType entryType,
        String ipAddress
    ) {
        this.id = id;
        this.subscriptionId = subscriptionId;
        this.time = time;
        this.entryType = entryType;
        this.ipAddress = ipAddress;
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
