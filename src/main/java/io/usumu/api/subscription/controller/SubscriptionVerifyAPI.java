package io.usumu.api.subscription.controller;

import io.swagger.annotations.*;
import io.usumu.api.common.entity.ApiError;
import io.usumu.api.subscription.entity.Subscription;
import io.usumu.api.subscription.exception.SubscriptionNotFound;
import io.usumu.api.subscription.exception.VerificationFailed;
import org.springframework.web.bind.annotation.*;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@RestController
@Api(
        tags = "Subscriptions"
)
public class SubscriptionVerifyAPI {
    @ApiResponses({
            @ApiResponse(code = 200, message = "Verification successful.", response = SubscriptionVerifyResponse.class),
            @ApiResponse(code = 400, message = "The verification code submitted is invalid, verification failed.", response = ApiError.class),
            @ApiResponse(code = 404, message = "The subscription with the e-mail or phone number does not exist.", response = ApiError.class),
    })
    @ApiOperation(
            nickname = "verifySubscription",
            value = "Verify a subscription",
            notes = "Verify a subscription by providing a verification code.",
            consumes = "application/json",
            produces = "application/json"
    )
    @RequestMapping(value = "/subscriptions/{value}", method = RequestMethod.PATCH)
    public SubscriptionVerifyResponse verify(
            @ApiParam(
                    value = "Subscriber ID, or subscriber contact info (EMAIL or PHONE in international format)",
                    required = true
            )
                    @PathVariable
                    String value,
            @ApiParam(
                    value = "Verification code for the address.",
                    required = true
            )
                    @RequestParam
                    String verificationCode
    ) throws SubscriptionNotFound, VerificationFailed {
        return null;
    }

    public static class SubscriptionVerifyResponse {
        public final Subscription subscription;

        public SubscriptionVerifyResponse(Subscription subscription) {
            this.subscription = subscription;
        }
    }
}
