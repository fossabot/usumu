package io.usumu.api.log.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.usumu.api.common.entity.EncryptedEntity;
import io.usumu.api.crypto.EntityCrypto;

public class EncryptedLogEntry extends EncryptedEntity<LogEntry> {
    public EncryptedLogEntry(LogEntry logEntry, EntityCrypto entityCrypto) {
        super(logEntry.id, logEntry, entityCrypto);
    }

    @JsonCreator
    public EncryptedLogEntry(
        @JsonProperty("hash")
            String hash,
        @JsonProperty("encryptedData")
            String encryptedData
    ) {
        super(hash, encryptedData);
    }
}
