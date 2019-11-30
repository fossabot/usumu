package io.usumu.api.subscription.controller;

import io.usumu.api.subscription.resource.SubscriptionResource;

public class SubscriptionVerifyResponse {
    public final SubscriptionResource subscription;

    public SubscriptionVerifyResponse(SubscriptionResource subscription) {
        this.subscription = subscription;
    }
}
