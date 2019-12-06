package io.usumu.api.subscription.controller;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.*;
import io.usumu.api.subscription.entity.SubscriptionMethod;

@ApiModel(description = "This request object contains all parameters required to create a subscription.")
public class SubscriptionCreateRequest {
    @ApiModelProperty(
        value = "Subscriber method.",
        required = true,
        example = "EMAIL"
    )
    public final SubscriptionMethod method;
    @ApiModelProperty(
        value = "Subscriber contact info, EMAIL or SMS",
        required = true,
        example = "subscriber@example.com"
    )
    public final String value;

    @JsonCreator
    public SubscriptionCreateRequest(
        @JsonProperty("method")
            SubscriptionMethod method,
        @JsonProperty("value")
        String value
    ) {
        this.method = method;

        this.value = value;
    }
}
