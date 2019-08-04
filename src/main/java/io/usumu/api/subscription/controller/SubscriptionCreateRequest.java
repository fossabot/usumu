package io.usumu.api.subscription.controller;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;
import io.usumu.api.subscription.entity.Subscription;

public class SubscriptionCreateRequest {
    public final Subscription.Type type;
    public final String value;

    @JsonCreator
    public SubscriptionCreateRequest(
        @ApiParam(
            value = "Subscriber type.",
            required = true,
            type = "body",
            examples = @Example({
                @ExampleProperty("EMAIL"),
                @ExampleProperty("SMS")
            })
        )
        @JsonProperty("type")
        Subscription.Type type,
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
        this.type = type;

        this.value = value;
    }
}
