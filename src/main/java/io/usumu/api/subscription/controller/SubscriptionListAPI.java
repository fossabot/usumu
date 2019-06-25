package io.usumu.api.subscription.controller;

import io.usumu.api.subscription.entity.Subscription;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@RestController
public class SubscriptionListAPI {
    @RequestMapping(
            value = "/subscriptions",
            method = RequestMethod.GET
    )
    public SubscriptionListResponse list(
    ) {
        return null;
    }

    public static class SubscriptionListResponse {
        private final List<Subscription> subscriptions;

        public SubscriptionListResponse(List<Subscription> subscriptions) {
            this.subscriptions = subscriptions;
        }

        public List<Subscription> getSubscriptions() {
            return subscriptions;
        }
    }
}
