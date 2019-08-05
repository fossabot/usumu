package io.usumu.api.common.validation;

import org.springframework.lang.Nullable;

public class MaximumValidator implements Validator {
    private int maximum;

    public MaximumValidator(int maximum) {
        this.maximum = maximum;
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
                return Integer.parseInt((String)value) <= maximum;
            } catch (NumberFormatException e) {
                // Not a number, can't validate. Use IntegerValidator.
                return true;
            }
        } else if (value instanceof Integer) {
            return (Integer)value <= maximum;
        } else {
            return false;
        }
    }

    @Override
    public String getUniqueKey() {
        return "maximum";
    }

    @Override
    public String getDescription() {
        return "This field must contain a number lower or equal than " + maximum;
    }
}
