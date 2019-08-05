package io.usumu.api.common.validation;

import org.springframework.lang.Nullable;

public class MinimumLengthValidator implements Validator {
    private int minimumLength;

    public MinimumLengthValidator(int minimumLength) {
        this.minimumLength = minimumLength;
    }

    @Override
    public boolean isValid(@Nullable Object value) {
        if (value == null) {
            return true;
        }
        if (value instanceof String) {
            return ((String)value).length() >= minimumLength;
        } else {
            return false;
        }
    }

    @Override
    public String getUniqueKey() {
        return "minimum-length";
    }

    @Override
    public String getDescription() {
        return "This field must be at least " + minimumLength + " characters long.";
    }
}
