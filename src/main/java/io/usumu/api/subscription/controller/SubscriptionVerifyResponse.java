package io.usumu.api.subscription.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import io.usumu.api.subscription.resource.SubscriptionResource;

public class SubscriptionVerifyResponse {
    @ApiModelProperty(value = "subscription", required = true)
    @JsonProperty(value = "subscription", required = true)
    public final SubscriptionResource subscription;

    public SubscriptionVerifyResponse(SubscriptionResource subscription) {
        this.subscription = subscription;
    }
}
