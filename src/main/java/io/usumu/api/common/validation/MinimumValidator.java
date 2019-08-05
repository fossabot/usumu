package io.usumu.api.common.validation;

import org.springframework.lang.Nullable;

public class MinimumValidator implements Validator {
    private int minimum;

    public MinimumValidator(int minimum) {
        this.minimum = minimum;
    }

    @Override
    public boolean isValid(@Nullable Object value) {
        if (value == null) {
            return true;
        }
        if (value instanceof String) {
            if (((String) value).isEmpty()) {
                return true;
            }
            try {
                return Integer.parseInt((String)value) >= minimum;
            } catch (NumberFormatException e) {
                // Not a number, can't validate. Use IntegerValidator.
                return true;
            }
        } else if (value instanceof Integer) {
            return (Integer)value >= minimum;
        } else {
            return false;
        }
    }

    @Override
    public String getUniqueKey() {
        return "minimum";
    }

    @Override
    public String getDescription() {
        return "This field must contain a number larger or equal than " + minimum;
    }
}
