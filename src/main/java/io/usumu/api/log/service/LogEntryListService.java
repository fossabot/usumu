package io.usumu.api.log.service;

import io.usumu.api.common.entity.PaginatedList;
import io.usumu.api.crypto.EntityCrypto;
import io.usumu.api.log.entity.EncryptedLogEntry;
import io.usumu.api.log.entity.LogEntry;
import io.usumu.api.log.storage.LogStorageList;
import io.usumu.api.subscription.entity.Subscription;
import io.usumu.api.subscription.exception.DecryptionFailed;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LogEntryListService {
    private final LogStorageList logStorageList;
    private final EntityCrypto entityCrypto;

    public LogEntryListService(LogStorageList logStorageList, EntityCrypto entityCrypto) {
        this.logStorageList = logStorageList;
        this.entityCrypto = entityCrypto;
    }

    public PaginatedList<LogEntry> list(
        Subscription subscription,
        @Nullable
            Integer itemCount,
        String continuationToken
    ) {
        PaginatedList<EncryptedLogEntry> encryptedSubscriptions = logStorageList
            .list(subscription.id, itemCount == null ? 100 : itemCount, continuationToken);

        List<LogEntry> logEntries = encryptedSubscriptions
            .items
            .stream()
            .map(encryptedSubscription -> {
                try {
                    return entityCrypto.decrypt(encryptedSubscription.encryptedData, LogEntry.class);
                } catch (DecryptionFailed decryptionFailed) {
                    throw new RuntimeException(decryptionFailed);
                }
            })
            .collect(Collectors.toList());
        return new PaginatedList<>(
            encryptedSubscriptions.continuationToken,
            logEntries
        );
    }
}
