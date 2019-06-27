package io.usumu.api.common.entity;

public class ApiError {
    public final String errorCode;
    public final String errorMessage;

    public ApiError(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
