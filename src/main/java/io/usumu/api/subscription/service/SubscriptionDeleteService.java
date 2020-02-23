package io.usumu.api.subscription.service;

import io.usumu.api.crypto.EntityCrypto;
import io.usumu.api.subscription.entity.EncryptedSubscription;
import io.usumu.api.subscription.entity.Subscription;
import io.usumu.api.subscription.entity.SubscriptionStatus;
import io.usumu.api.subscription.exception.SubscriptionAlreadyDeleted;
import io.usumu.api.subscription.storage.SubscriptionStorageUpsert;
import org.springframework.stereotype.Service;

import javax.websocket.server.ServerEndpoint;

@Service
public class SubscriptionDeleteService {
    private final SubscriptionStorageUpsert subscriptionStorageUpsert;
    private final EntityCrypto entityCrypto;

    public SubscriptionDeleteService(SubscriptionStorageUpsert subscriptionStorageUpsert, EntityCrypto entityCrypto) {
        this.subscriptionStorageUpsert = subscriptionStorageUpsert;
        this.entityCrypto = entityCrypto;
    }

    public Subscription delete(Subscription subscription) throws SubscriptionAlreadyDeleted {
        if (subscription.status.equals(SubscriptionStatus.UNSUBSCRIBED)) {
            throw new SubscriptionAlreadyDeleted();
        }
        subscription = subscription.withUnsubscribed();
        subscriptionStorageUpsert.store(new EncryptedSubscription(subscription, entityCrypto));
        return subscription;
    }
}
