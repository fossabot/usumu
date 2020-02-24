package io.usumu.api.subscription.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;

import javax.annotation.Nullable;

public class SubscriptionVerifyRequest {
    @SuppressWarnings("WeakerAccess")
    @ApiModelProperty(
        value = "Verification code for the address.",
        required = true
    )
    @JsonProperty(value = "verificationCode", required = true)
    public final String verificationCode;
    @Nullable
    @ApiModelProperty(
        value = "Remote IP address for logging purposes",
        required = false
    )
    @JsonProperty(value = "remoteIp")
    public final String remoteIp;

    public SubscriptionVerifyRequest(
        @ApiParam(
            value = "Verification code for the address.",
            required = true
        )
        @JsonProperty(value = "verificationCode")
            String verificationCode,
        @Nullable
        @ApiParam(
            value = "Remote IP address for logging purposes",
            required = false
        )
        @JsonProperty(value = "remoteIp")
        final String remoteIp
    ) {
        this.verificationCode = verificationCode;
        this.remoteIp = remoteIp;
    }
}
