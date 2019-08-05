package io.usumu.api.common.validation;

import org.springframework.lang.Nullable;

import java.util.List;

public class InListValidator implements Validator {
    private final List<String> expected;

    public InListValidator(List<String> expected) {
        this.expected = expected;
    }

    @Override
    public String getUniqueKey() {
        return "in-list";
    }

    @Override
    public String getDescription() {
        return "This field must be one of " + String.join(", ", expected);
    }

    @Override
    public boolean isValid(@Nullable Object value) {
        return expected.contains(value);
    }
}
