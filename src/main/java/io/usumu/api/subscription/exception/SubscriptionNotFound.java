package io.usumu.api.subscription.exception;

import io.usumu.api.common.entity.ErrorCode;
import io.usumu.api.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class SubscriptionNotFound extends ApiException {
    public SubscriptionNotFound() {
        super(HttpStatus.NOT_FOUND, ErrorCode.SUBSCRIPTION_NOT_FOUND, "The specified subscription was not found.");
    }
}
