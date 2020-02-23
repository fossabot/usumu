package io.usumu.api.log.service;

import io.usumu.api.crypto.EntityCrypto;
import io.usumu.api.log.entity.EncryptedLogEntry;
import io.usumu.api.log.entity.LogEntry;
import io.usumu.api.log.storage.LogStorageUpsert;
import io.usumu.api.subscription.entity.Subscription;
import org.springframework.stereotype.Service;

@Service
public class LogEntryCreateService {
    private final LogStorageUpsert logStorageUpsert;
    private final EntityCrypto entityCrypto;

    public LogEntryCreateService(LogStorageUpsert logStorageUpsert, EntityCrypto entityCrypto) {
        this.logStorageUpsert = logStorageUpsert;
        this.entityCrypto = entityCrypto;
    }

    public void create(Subscription subscription, LogEntry logEntry) {
        logStorageUpsert.store(subscription.id, new EncryptedLogEntry(logEntry, entityCrypto));
    }
}
