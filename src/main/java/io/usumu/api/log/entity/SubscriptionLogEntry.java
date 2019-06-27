package io.usumu.api.log.entity;

import javax.annotation.Nullable;
import java.time.LocalDateTime;

public class SubscriptionLogEntry {
    public final String id;
    public final LocalDateTime time;
    public final Type type;
    public final String ipAddress;
    @Nullable
    public final String newsletterId;


    public SubscriptionLogEntry(String id, LocalDateTime time, Type type, String ipAddress, @Nullable String newsletterId) {
        this.id = id;
        this.time = time;
        this.type = type;
        this.ipAddress = ipAddress;
        this.newsletterId = newsletterId;
    }

    public enum Type {
        CREATED,
        VERIFIED,
        NEWSLETTER,
        DELETED
    }
}
