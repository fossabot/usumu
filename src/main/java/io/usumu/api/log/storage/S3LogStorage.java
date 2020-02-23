package io.usumu.api.log.storage;

import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import io.usumu.api.common.entity.PaginatedList;
import io.usumu.api.log.entity.EncryptedLogEntry;
import io.usumu.api.log.exception.LogEntryNotFound;
import io.usumu.api.s3.S3Accessor;
import io.usumu.api.s3.S3Storage;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class S3LogStorage implements LogStorageGet, LogStorageList, LogStorageUpsert {
    private final S3Storage s3Storage;

    public S3LogStorage(S3Storage s3Storage) {
        this.s3Storage = s3Storage;
    }

    @Override
    public EncryptedLogEntry get(String subscriptionId, String entryId) throws LogEntryNotFound {
        try {
            return s3Storage.get("l/" + subscriptionId + "/" + entryId, EncryptedLogEntry.class);
        } catch (S3Accessor.ObjectNotFoundException e) {
            throw new LogEntryNotFound();
        }
    }

    @Override
    public PaginatedList<EncryptedLogEntry> list(
            String subscriptionId,
            int maxItems,
            String continuationToken
    ) {
        return s3Storage.list("l/" + subscriptionId + "/", maxItems, continuationToken, EncryptedLogEntry.class);
    }

    @Override
    public void store(String subscriptionId, EncryptedLogEntry entry) {
        s3Storage.store(entry, "l/" + subscriptionId + "/" + entry.hash);
    }
}
