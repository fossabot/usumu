package io.usumu.api.common.validation;

import org.springframework.lang.Nullable;

public class RequiredValidator implements Validator {
    @Override
    public String getUniqueKey() {
        return "required";
    }

    @Override
    public String getDescription() {
        return "This field is required.";
    }

    @Override
    public boolean isValid(@Nullable Object value) {
        if (value != null && (value instanceof Integer || value instanceof Boolean || value.getClass().isEnum())) {
            return true;
        }
        if (value instanceof String) {
            return !value.equals("");
        }
        return false;
    }
}
