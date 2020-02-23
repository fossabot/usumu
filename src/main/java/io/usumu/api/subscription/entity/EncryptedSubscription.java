package io.usumu.api.subscription.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.usumu.api.common.entity.EncryptedEntity;
import io.usumu.api.crypto.EntityCrypto;
import io.usumu.api.log.entity.LogEntry;

public class EncryptedSubscription extends EncryptedEntity<Subscription> {
    public EncryptedSubscription(Subscription subscription, EntityCrypto entityCrypto) {
        super(subscription.id, subscription, entityCrypto);
    }

    @JsonCreator
    public EncryptedSubscription(
            @JsonProperty("hash")
            String hash,    
            @JsonProperty("encryptedData")
            String encryptedData
    ) {
        super(hash, encryptedData);
    }
}