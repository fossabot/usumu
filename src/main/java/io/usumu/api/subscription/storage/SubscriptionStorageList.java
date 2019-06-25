package io.usumu.api.subscription.storage;

import io.usumu.api.subscription.entity.EncryptedSubscription;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;

@ParametersAreNonnullByDefault
public interface SubscriptionStorageList {
    Collection<EncryptedSubscription> list();
}
