package io.usumu.api.common.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.usumu.api.crypto.EntityCrypto;

public class EncryptedEntity<T> {
    @JsonProperty("hash")
    public final String hash;
    @JsonProperty("encryptedData")
    public final String encryptedData;

    public EncryptedEntity(
            String hash,
            T entity,
            EntityCrypto entityCrypto
    ) {
        this.hash = hash;
        encryptedData = entityCrypto.encrypt(entity);
    }

    @JsonCreator
    public EncryptedEntity(
            @JsonProperty("hash")
                    String hash,
            @JsonProperty("encryptedData")
                    String encryptedData
    ) {
        this.hash = hash;
        this.encryptedData = encryptedData;
    }
}
