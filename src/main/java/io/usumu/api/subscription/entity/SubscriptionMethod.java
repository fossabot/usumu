package io.usumu.api.subscription.entity;

import java.security.InvalidParameterException;

public enum SubscriptionMethod {
    EMAIL,
    SMS;

    public static SubscriptionMethod fromString(String method) {
        for (SubscriptionMethod value : SubscriptionMethod.values()) {
            if (value.toString().equalsIgnoreCase(method)) {
                return value;
            }
        }
        throw new InvalidParameterException();
    }
}
