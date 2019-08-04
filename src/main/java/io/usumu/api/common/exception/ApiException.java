package io.usumu.api.common.exception;

import io.usumu.api.common.entity.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ApiException extends ResponseStatusException {
    public final ApiError.ErrorCode errorCode;
    public final String errorMessage;

    public ApiException(HttpStatus httpStatus, ApiError.ErrorCode errorCode, String errorMessage) {
        super(httpStatus, errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
