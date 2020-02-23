package io.usumu.api.subscription.service;

import io.usumu.api.crypto.EntityCrypto;
import io.usumu.api.subscription.entity.EncryptedSubscription;
import io.usumu.api.subscription.entity.Subscription;
import io.usumu.api.subscription.storage.SubscriptionStorageUpsert;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionUpdateService {
    private final SubscriptionStorageUpsert subscriptionStorageUpsert;
    private final EntityCrypto entityCrypto;

    public SubscriptionUpdateService(SubscriptionStorageUpsert subscriptionStorageUpsert, EntityCrypto entityCrypto) {
        this.subscriptionStorageUpsert = subscriptionStorageUpsert;
        this.entityCrypto = entityCrypto;
    }

    public void update(Subscription subscription) {
        subscriptionStorageUpsert.store(new EncryptedSubscription(subscription, entityCrypto));
    }
}
