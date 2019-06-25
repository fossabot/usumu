package io.usumu.api.subscription.controller;

import io.usumu.api.subscription.exception.SubscriptionNotFound;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.usumu.api.subscription.entity.Subscription;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@RestController
public class SubscriptionDeleteAPI {
    @RequestMapping(
            value = "/subscriptions/:subscriptionId",
            method = RequestMethod.DELETE
    )
    public SubscriptionDeleteResponse delete(
            String subscriptionId
    ) throws SubscriptionNotFound {
        return null;
    }

    public static class SubscriptionDeleteResponse {
        public final Subscription subscription;

        public SubscriptionDeleteResponse(Subscription subscription) {
            this.subscription = subscription;
        }
    }
}
