package io.usumu.api.subscription.entity;

import java.security.InvalidParameterException;

public enum SubscriptionStatus {
    UNCONFIRMED,
    CONFIRMED,
    UNSUBSCRIBED;

    public static SubscriptionStatus fromString(String status) {
        for (SubscriptionStatus value : SubscriptionStatus.values()) {
            if (value.toString().equalsIgnoreCase(status)) {
                return value;
            }
        }
        throw new InvalidParameterException();
    }
}
