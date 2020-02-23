package io.usumu.api.log.storage;

import io.usumu.api.log.entity.LogEntry;

public interface LogStorageGet {
    LogEntry get(String entryId);
}
