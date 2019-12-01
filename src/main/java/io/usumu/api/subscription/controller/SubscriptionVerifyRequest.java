package io.usumu.api.subscription.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestParam;

public class SubscriptionVerifyRequest {
    @JsonProperty("verificationCode")
    public final String verificationCode;

    public SubscriptionVerifyRequest(
        @ApiParam(
            value = "Verification code for the address.",
            required = true
        )
        @JsonProperty(value = "verificationCode", required = false)
        String verificationCode
    ) {
        this.verificationCode = verificationCode;
    }
}
