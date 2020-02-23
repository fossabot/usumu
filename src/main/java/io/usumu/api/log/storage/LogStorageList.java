package io.usumu.api.log.storage;

import io.usumu.api.common.entity.PaginatedList;
import io.usumu.api.log.entity.EncryptedLogEntry;
import io.usumu.api.log.entity.LogEntry;

import java.util.List;

public interface LogStorageList {
    PaginatedList<EncryptedLogEntry> list(
            String subscriptionId,
            int maxItems,
            String continuationToken
    );
}
