package io.usumu.api.common.entity;

public class ApiError {
    public final ErrorCode errorCode;
    public final String errorMessage;

    public ApiError(ErrorCode errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public enum ErrorCode {
        SUBSCRIPTION_ALREADY_EXISTS,
        SUBSCRIPTION_NOT_FOUND,
        INVALID_PARAMETERS
    }
}
