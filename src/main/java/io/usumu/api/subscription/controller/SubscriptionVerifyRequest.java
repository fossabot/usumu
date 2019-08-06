package io.usumu.api.subscription.controller;

import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestParam;

public class SubscriptionVerifyRequest {
    final String verificationCode;

    public SubscriptionVerifyRequest(
        @ApiParam(
            value = "Verification code for the address.",
            required = true
        )
        @RequestParam
        String verificationCode
    ) {

        this.verificationCode = verificationCode;
    }
}
