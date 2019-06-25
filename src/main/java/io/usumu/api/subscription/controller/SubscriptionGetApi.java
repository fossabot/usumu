package io.usumu.api.subscription.controller;

import io.usumu.api.subscription.entity.Subscription;
import io.usumu.api.subscription.exception.SubscriptionNotFound;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@RestController
public class SubscriptionGetApi {
    @RequestMapping(
            value = "/subscriptions/:value",
            method = RequestMethod.GET
    )
    public SubscriptionGetResponse get(
        String value
    ) throws SubscriptionNotFound {
        return null;
    }

    public static class SubscriptionGetResponse {
        public final Subscription subscription;

        public SubscriptionGetResponse(Subscription subscription) {
            this.subscription = subscription;
        }
    }
}
