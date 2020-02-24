package io.usumu.api.subscription.controller;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.*;
import io.usumu.api.subscription.entity.SubscriptionMethod;
import io.usumu.api.subscription.entity.SubscriptionStatus;

import javax.annotation.Nullable;

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

    @ApiModelProperty(
        value = "Import the subscription instead of creating it. The import process will bypass any e-mails being sent out.",
        required = false,
        example = "true"
    )
    public final Boolean imported;

    @Nullable
    @ApiModelProperty(
        value = "Import the subscription with a specific status.",
        required = false,
        example = "CONFIRMED"
    )
    public final SubscriptionStatus importStatus;
    @Nullable
    @ApiModelProperty(
        value = "Remote IP address for logging purposes",
        required = false
    )
    @JsonProperty(value = "remote_ip", required = false)
    public final String remoteIp;

    @JsonCreator
    public SubscriptionCreateRequest(
        @JsonProperty(value = "method", required = true)
            SubscriptionMethod method,
        @JsonProperty(value = "value", required = true)
            String value,
        @Nullable
        @JsonProperty("import")
            Boolean imported,
        @Nullable
        @JsonProperty("import_status")
            SubscriptionStatus importStatus,
        @Nullable
        @JsonProperty(value = "remote_ip", required = false)
        String remoteIp
    ) {
        this.method = method;
        this.value = value;
        this.imported = imported == null?false:imported;
        this.importStatus = importStatus;
        this.remoteIp = remoteIp;
    }
}
