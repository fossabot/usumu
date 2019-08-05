package io.usumu.api.common.validation;

public interface Validator {
    String getUniqueKey();
    String getDescription();
    boolean isValid(Object value);
}
