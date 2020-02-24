package io.usumu.api.subscription.controller;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;

import javax.annotation.Nullable;

public class SubscriptionDeleteRequest {
    @Nullable
    @ApiModelProperty(
        value = "Remote IP address for logging purposes",
        required = false
    )
    @JsonProperty(value = "remoteIp", required = false)
    public final String remoteIp;

    @JsonCreator
    public SubscriptionDeleteRequest(
        @Nullable
        @ApiParam(
            value = "Remote IP address for logging purposes",
            required = false
        )
        @JsonProperty(value = "remoteIp", required = false)
        final String remoteIp
    ) {
        this.remoteIp = remoteIp;
    }
}
