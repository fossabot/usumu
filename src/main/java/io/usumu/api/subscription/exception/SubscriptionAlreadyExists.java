package io.usumu.api.subscription.exception;

import io.usumu.api.common.entity.ApiError;
import io.usumu.api.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class SubscriptionAlreadyExists extends ApiException {
    public SubscriptionAlreadyExists() {
        super(HttpStatus.CONFLICT, ApiError.ErrorCode.SUBSCRIPTION_ALREADY_EXISTS, "The specified subscription already exists.");
    }

}
