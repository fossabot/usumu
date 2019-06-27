package io.usumu.api.common.controller;

import io.usumu.api.common.entity.ApiError;
import io.usumu.api.common.exception.ApiException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiError> exception(ApiException exception) {
        return new ResponseEntity<>(
                new ApiError(
                        exception.errorCode,
                        exception.errorMessage
                ),
                exception.getStatus()
        );
    }
}
