package io.usumu.api.log.storage;

import io.usumu.api.log.entity.EncryptedLogEntry;
import io.usumu.api.log.entity.LogEntry;
import io.usumu.api.log.exception.LogEntryNotFound;

public interface LogStorageGet {
    EncryptedLogEntry get(String subscriptionId, String entryId) throws LogEntryNotFound;
}
