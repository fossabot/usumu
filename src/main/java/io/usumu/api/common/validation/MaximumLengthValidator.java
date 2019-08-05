package io.usumu.api.common.validation;

import org.springframework.lang.Nullable;

public class MaximumLengthValidator implements Validator {
    private int maximumLength;

    public MaximumLengthValidator(int maximumLength) {
        this.maximumLength = maximumLength;
    }

    @Override
    public boolean isValid(@Nullable Object value) {
        if (value instanceof String) {
            return ((String)value).length() <= maximumLength;
        } else {
            return false;
        }
    }

    @Override
    public String getUniqueKey() {
        return "maximum-length";
    }

    @Override
    public String getDescription() {
        return "This field must contain at most " + maximumLength + " characters";
    }
}
