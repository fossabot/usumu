package io.usumu.api.subscription.exception;

import io.usumu.api.common.exception.ApiException;
import org.springframework.http.HttpStatus;

import static io.usumu.api.common.entity.ErrorCode.SUBSCRIPTION_ALREADY_VERIFIED;

public class SubscriptionAlreadyVerified extends ApiException {
    public SubscriptionAlreadyVerified() {
        super(HttpStatus.CONFLICT, SUBSCRIPTION_ALREADY_VERIFIED, "This subscription has already been verified.");
    }
}
