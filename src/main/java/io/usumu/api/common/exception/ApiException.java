package io.usumu.api.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ApiException extends ResponseStatusException {
    public final String errorCode;
    public final String errorMessage;

    public ApiException(HttpStatus httpStatus, String errorCode, String errorMessage) {
        super(httpStatus, errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
