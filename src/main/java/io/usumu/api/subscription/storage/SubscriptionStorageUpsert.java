package io.usumu.api.subscription.storage;

import io.usumu.api.subscription.entity.EncryptedSubscription;

public interface SubscriptionStorageUpsert {
    void store(EncryptedSubscription encryptedSubscription);
}
