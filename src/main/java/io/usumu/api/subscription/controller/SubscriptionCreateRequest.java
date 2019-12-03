package io.usumu.api.subscription.controller;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;
import io.usumu.api.subscription.entity.Subscription;

public class SubscriptionCreateRequest {
    public final Subscription.Method method;
    public final String value;

    @JsonCreator
    public SubscriptionCreateRequest(
        @ApiParam(
            value = "Subscriber method.",
            required = true,
            type = "body",
            examples = @Example({
                @ExampleProperty("EMAIL"),
                @ExampleProperty("SMS")
            })
        )
        @JsonProperty("method")
        Subscription.Method method,
        @ApiParam(
            value = "Subscriber contact info, EMAIL or SMS",
            required = true,
            type = "body",
            examples = @Example({
                @ExampleProperty("subscriber@example.com"),
                @ExampleProperty("+123456789")
            })
        )
        @JsonProperty("value")
            String value
    ) {
        this.method = method;

        this.value = value;
    }
}
