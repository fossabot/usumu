package io.usumu.api.subscription.storage;

import io.usumu.api.subscription.entity.EncryptedSubscription;
import io.usumu.api.subscription.exception.SubscriptionNotFound;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface SubscriptionStorageGet {
    EncryptedSubscription get(String hash) throws SubscriptionNotFound;
}
