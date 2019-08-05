package io.usumu.api.common.validation;

import org.springframework.lang.Nullable;

import java.util.regex.Pattern;

public class PatternValidator implements Validator {

    private final Pattern pattern;

    public PatternValidator(Pattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public String getUniqueKey() {
        return "pattern";
    }

    @Override
    public String getDescription() {
        return "This field must match the pattern " + pattern.toString();
    }

    @Override
    public boolean isValid(@Nullable Object value) {
        if (value == null) {
            return true;
        }
        if (value instanceof String) {
            return ((String) value).isEmpty() || pattern.matcher((String)value).matches();
        }
        return false;
    }
}
