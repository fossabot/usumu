package io.usumu.api.subscription.service;

import io.usumu.api.common.entity.PaginatedList;
import io.usumu.api.crypto.EntityCrypto;
import io.usumu.api.subscription.entity.EncryptedSubscription;
import io.usumu.api.subscription.entity.Subscription;
import io.usumu.api.subscription.exception.DecryptionFailed;
import io.usumu.api.subscription.storage.SubscriptionStorageList;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriptionListService {
    private final EntityCrypto entityCrypto;
    private final SubscriptionStorageList subscriptionStorageList;

    public SubscriptionListService(
            EntityCrypto entityCrypto,
            SubscriptionStorageList subscriptionStorageList
    ) {
        this.entityCrypto = entityCrypto;
        this.subscriptionStorageList = subscriptionStorageList;
    }

    public PaginatedList<Subscription> list(
            @Nullable
            Integer itemCount,
            String continuationToken
    ) {
        PaginatedList<EncryptedSubscription> encryptedSubscriptions = subscriptionStorageList
                .list(itemCount == null ? 100 : itemCount, continuationToken);

        List<Subscription> subscriptions = encryptedSubscriptions
                .items
                .stream()
                .map(encryptedSubscription -> {
                    try {
                        return entityCrypto.decrypt(encryptedSubscription.encryptedData, Subscription.class);
                    } catch (DecryptionFailed decryptionFailed) {
                        throw new RuntimeException(decryptionFailed);
                    }
                })
                .collect(Collectors.toList());
        return new PaginatedList<>(
            encryptedSubscriptions.continuationToken,
            subscriptions
        );
    }
}
