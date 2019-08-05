package io.usumu.api.common.validation;

import org.springframework.lang.Nullable;

public class SingleLineValidator implements Validator {
    @Override
    public String getUniqueKey() {
        return "single-line";
    }

    @Override
    public String getDescription() {
        return "This field must not contain a line break.";
    }

    @Override
    public boolean isValid(@Nullable Object value) {
        if (value == null) {
            return true;
        }
        if (value instanceof String) {
            return !((String)value).contains("\n");
        } else {
            return false;
        }
    }
}
