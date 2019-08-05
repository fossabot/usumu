package io.usumu.api.common.validation;

import org.springframework.lang.Nullable;

import java.util.regex.Pattern;

public class FormalEmailValidator implements EmailValidator {
    private final static Pattern localPartPattern = Pattern.compile("\\A[a-zA-Z0-9\\-_+.\\x{007F}-\\x{FFFF}]+\\z", Pattern.UNICODE_CASE);
    private final boolean allowEmpty;
    private final DomainNameValidator domainNameValidator;

    public FormalEmailValidator() {
        this.domainNameValidator = new FormalDomainNameValidator();
        allowEmpty = false;
    }

    public FormalEmailValidator(DomainNameValidator domainNameValidator) {
        this.domainNameValidator = domainNameValidator;
        allowEmpty = false;
    }

    public FormalEmailValidator(boolean allowEmpty) {
        this.allowEmpty = allowEmpty;
        domainNameValidator = new FormalDomainNameValidator();
    }

    public FormalEmailValidator(boolean allowEmpty, DomainNameValidator domainNameValidator) {
        this.allowEmpty = allowEmpty;
        this.domainNameValidator = domainNameValidator;
    }

    @Override
    public String getUniqueKey() {
        return "email-format";
    }

    @Override
    public String getDescription() {
        return "This field must contain a valid e-mail address.";
    }

    @Override
    public boolean isValid(@Nullable Object value) {
        if (allowEmpty && (!(value instanceof String) || value.equals(""))) {
            return true;
        }

        if (((String)value).contains("\n")) {
            return false;
        }

        if (!((String)value).contains("@")) {
            return false;
        }

        String[] parts     = ((String)value).split("@", 2);
        String   localPart = parts[0];
        String   domain    = parts[1];

        return localPartPattern.matcher(localPart).matches() && domainNameValidator.isValid(domain);
    }

}
