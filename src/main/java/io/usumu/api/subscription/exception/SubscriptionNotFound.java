package io.usumu.api.subscription.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class SubscriptionNotFound extends Exception {
}
