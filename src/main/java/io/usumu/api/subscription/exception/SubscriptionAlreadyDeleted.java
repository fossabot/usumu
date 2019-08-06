package io.usumu.api.subscription.exception;

import io.usumu.api.common.entity.ErrorCode;
import io.usumu.api.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class SubscriptionAlreadyDeleted extends ApiException {
    public SubscriptionAlreadyDeleted() {
        super(HttpStatus.GONE, ErrorCode.SUBSCRIPTION_ALREADY_DELETED, "This subscription was already deleted before.");
    }
}
