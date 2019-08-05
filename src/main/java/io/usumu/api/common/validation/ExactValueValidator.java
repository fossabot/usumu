package io.usumu.api.common.validation;

import org.springframework.lang.Nullable;

public class ExactValueValidator implements Validator {
    private String expected;

    public ExactValueValidator(String expected) {
        this.expected = expected;
    }

    @Override
    public String getUniqueKey() {
        return "exact-value";
    }

    @Override
    public String getDescription() {
        return "This value must be exactly '" + expected + "'";
    }

    @Override
    public boolean isValid(@Nullable Object value) {
        return value.equals(expected);
    }
}
