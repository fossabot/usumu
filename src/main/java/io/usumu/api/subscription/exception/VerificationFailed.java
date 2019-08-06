package io.usumu.api.subscription.exception;

import io.usumu.api.common.entity.ErrorCode;
import io.usumu.api.common.exception.ApiException;
import org.springframework.http.HttpStatus;

public class VerificationFailed extends ApiException {
    public VerificationFailed() {
        super(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_VERIFICATION_CODE, "Invalid or expired verification code.");
    }
}
