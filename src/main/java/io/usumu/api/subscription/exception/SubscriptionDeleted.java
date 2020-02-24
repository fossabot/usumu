package io.usumu.api.subscription.exception;

import io.usumu.api.common.entity.ErrorCode;
import io.usumu.api.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class SubscriptionDeleted extends ApiException {
    public SubscriptionDeleted() {
        super(HttpStatus.GONE, ErrorCode.SUBSCRIPTION_DELETED, "This subscription no longer exists.");
    }
}
