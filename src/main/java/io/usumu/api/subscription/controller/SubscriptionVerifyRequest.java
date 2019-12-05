package io.usumu.api.subscription.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiParam;

public class SubscriptionVerifyRequest {
    @SuppressWarnings("WeakerAccess")
    @JsonProperty(value = "verificationCode", required = true)
    public final String verificationCode;

    public SubscriptionVerifyRequest(
        @ApiParam(
            value = "Verification code for the address.",
            required = true
        )
        @JsonProperty(value = "verificationCode")
        String verificationCode
    ) {
        this.verificationCode = verificationCode;
    }
}
