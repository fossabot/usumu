package io.usumu.api.common.validation;

public class Error {
    public final String key;
    public final String message;

    public Error(Validator validator) {
        this.key = validator.getUniqueKey();
        this.message = validator.getDescription();
    }
}
