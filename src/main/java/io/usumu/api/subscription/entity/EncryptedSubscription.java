package io.usumu.api.subscription.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.usumu.api.crypto.EntityCrypto;

public class EncryptedSubscription {
    public final String hash;
    public final String encryptedData;

    public EncryptedSubscription(
            Subscription subscription,
            EntityCrypto entityCrypto
    ) {

        hash = subscription.id;
        encryptedData = entityCrypto.encrypt(subscription);
    }

    @JsonCreator
    public EncryptedSubscription(
        @JsonProperty("hash")
            String hash,
            @JsonProperty("encryptedData")
            String encryptedData
    ) {
        this.hash = hash;
        this.encryptedData = encryptedData;
    }
}