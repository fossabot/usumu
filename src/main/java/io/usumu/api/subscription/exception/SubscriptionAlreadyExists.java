package io.usumu.api.subscription.exception;

import io.usumu.api.common.entity.ErrorCode;
import io.usumu.api.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class SubscriptionAlreadyExists extends ApiException {
    public SubscriptionAlreadyExists() {
        super(HttpStatus.CONFLICT, ErrorCode.ALREADY_EXISTS, "The specified subscription already exists.");
    }

}
