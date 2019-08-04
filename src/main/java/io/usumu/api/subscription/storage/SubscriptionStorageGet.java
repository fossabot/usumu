package io.usumu.api.subscription.storage;

import io.usumu.api.subscription.entity.EncryptedSubscription;
import io.usumu.api.subscription.exception.SubscriptionNotFound;

public interface SubscriptionStorageGet {
    EncryptedSubscription get(String hash) throws SubscriptionNotFound;
}
