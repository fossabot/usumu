package io.usumu.api.log.service;

import io.usumu.api.crypto.EntityCrypto;
import io.usumu.api.log.entity.LogEntry;
import io.usumu.api.log.exception.LogEntryNotFound;
import io.usumu.api.log.storage.LogStorageGet;
import io.usumu.api.subscription.entity.Subscription;
import org.springframework.stereotype.Service;

@Service
public class LogEntryGetService {
    private final LogStorageGet logStorageGet;
    private final EntityCrypto entityCrypto;

    public LogEntryGetService(LogStorageGet logStorageGet, EntityCrypto entityCrypto) {
        this.logStorageGet = logStorageGet;
        this.entityCrypto = entityCrypto;
    }

    public LogEntry get(Subscription subscription, String entryId) throws LogEntryNotFound {
        return entityCrypto.decrypt(logStorageGet.get(subscription.id, entryId).encryptedData, LogEntry.class);
    }
}
