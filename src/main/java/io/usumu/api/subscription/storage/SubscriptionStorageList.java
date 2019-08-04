package io.usumu.api.subscription.storage;

import io.usumu.api.common.entity.PaginatedList;
import io.usumu.api.subscription.entity.EncryptedSubscription;

public interface SubscriptionStorageList {
    PaginatedList<EncryptedSubscription> list(
        int maxItems,
        String continuationToken
    );


}
