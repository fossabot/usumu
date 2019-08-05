package io.usumu.api.common.controller;

import io.usumu.api.common.exception.ApiException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExoscaleExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({ ApiException.class })
    public ResponseEntity<ApiException> handleInvalidExoscaleCredentials(ApiException e) {
        ResponseEntity<ApiException> response = new ResponseEntity<>(
            e,
            e.httpStatus
        );

        response.getHeaders().addAll(e.headers);

        return response;
    }
}
