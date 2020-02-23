package io.usumu.api.log.exception;

import io.usumu.api.common.entity.ErrorCode;
import io.usumu.api.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class LogEntryNotFound extends ApiException {
    public LogEntryNotFound() {
        super(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND, "The specified log entry was not found.");
    }
}
