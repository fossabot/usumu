package io.usumu.api.common.validation;

import org.springframework.lang.Nullable;

public class IntegerValidator implements Validator {
    @Override
    public String getUniqueKey() {
        return "integer";
    }

    @Override
    public String getDescription() {
        return "This field must contain an integer (whole number)";
    }

    @Override
    public boolean isValid(@Nullable Object value) {
        if (value instanceof String) {
            try {
                Integer.parseInt((String)value);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        } else if (value instanceof Integer) {
            return true;
        } else {
            return false;
        }
    }
}
