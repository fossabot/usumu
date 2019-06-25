package io.usumu.api.subscription.storage;

import io.usumu.api.subscription.entity.EncryptedSubscription;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface SubscriptionStorageUpsert {
    void store(EncryptedSubscription encryptedSubscription);
}
