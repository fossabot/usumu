package io.usumu.api.subscription.controller;

import io.usumu.api.subscription.entity.Subscription;
import io.usumu.api.subscription.exception.SubscriptionNotFound;
import io.usumu.api.subscription.exception.VerificationFailed;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@RestController
public class SubscriptionVerifyAPI {
    @RequestMapping(value = "/subscriptions/:subscriptionId", method = RequestMethod.PATCH)
    public SubscriptionVerifyResponse verify(
        String subscriptionId,
        String verificationCode
    ) throws SubscriptionNotFound, VerificationFailed {
        return null;
    }

    public static class SubscriptionVerifyResponse {
        private final Subscription subscription;

        public SubscriptionVerifyResponse(Subscription subscription) {
            this.subscription = subscription;
        }

        public Subscription getSubscription() {
            return subscription;
        }
    }
}
