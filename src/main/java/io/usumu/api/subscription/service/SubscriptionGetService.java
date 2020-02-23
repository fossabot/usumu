package io.usumu.api.subscription.service;

import io.usumu.api.crypto.EntityCrypto;
import io.usumu.api.crypto.HashGenerator;
import io.usumu.api.subscription.entity.EncryptedSubscription;
import io.usumu.api.subscription.entity.Subscription;
import io.usumu.api.subscription.exception.SubscriptionNotFound;
import io.usumu.api.subscription.storage.SubscriptionStorageGet;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionGetService {
    private final EntityCrypto entityCrypto;
    private final SubscriptionStorageGet subscriptionStorageGet;
    private final HashGenerator hashGenerator;

    public SubscriptionGetService(
            EntityCrypto entityCrypto,
            SubscriptionStorageGet subscriptionStorageGet,
            HashGenerator hashGenerator
    ) {
        this.entityCrypto = entityCrypto;
        this.subscriptionStorageGet = subscriptionStorageGet;
        this.hashGenerator = hashGenerator;
    }

    public Subscription get(String valueOrHash) throws SubscriptionNotFound {
        EncryptedSubscription encryptedSubscription;
        try {
            encryptedSubscription = subscriptionStorageGet.get(hashGenerator.generateHash(valueOrHash));
        } catch (SubscriptionNotFound e) {
            encryptedSubscription = subscriptionStorageGet.get(valueOrHash);
        }
        return entityCrypto.decrypt(
                encryptedSubscription.encryptedData,
                Subscription.class
        );
    }
}
