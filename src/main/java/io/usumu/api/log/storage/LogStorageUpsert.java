package io.usumu.api.log.storage;

import io.usumu.api.log.entity.LogEntry;

public interface LogStorageUpsert {
    void store(String subscriptionId, LogEntry entry);
}
