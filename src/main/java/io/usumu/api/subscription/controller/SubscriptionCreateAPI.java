package io.usumu.api.subscription.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.usumu.api.subscription.entity.Subscription;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@RestController
public class SubscriptionCreateAPI {
    @ApiOperation(
            nickname = "createSubscription",
            value = "Create a subscription",
            notes = "Create a subscription by providing the subscription type (EMAIL or SMS) and the contact details." +
                    "The value in this case is either the phone number in international format, or the e-mail address." +
                    "When the subscription is created a confirmation message is sent to the user to confirm their" +
                    "subscription."
    )
    @RequestMapping(
            value = "/subscriptions",
            method = RequestMethod.POST
    )
    public SubscriptionCreateResponse create(
            @ApiParam(
                    value = "Subscriber type.",
                    required = true
            )
            Subscription.Type type,
            @ApiParam(
                    value = "Subscriber contact info, EMAIL or SMS",
                    required = true
            )
            String value
    ) {
        return null;
    }

    public static class SubscriptionCreateResponse {
        public final Subscription subscription;

        public SubscriptionCreateResponse(Subscription subscription) {
            this.subscription = subscription;
        }
    }
}
