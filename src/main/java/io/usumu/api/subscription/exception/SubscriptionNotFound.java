package io.usumu.api.subscription.exception;

import io.usumu.api.common.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SubscriptionNotFound extends ApiException {
    public SubscriptionNotFound() {
        super(HttpStatus.NOT_FOUND, "not-found", "The specified subscription was not found.");
    }
}
