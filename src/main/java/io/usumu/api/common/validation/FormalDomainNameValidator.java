package io.usumu.api.common.validation;

import org.springframework.lang.Nullable;

import java.net.IDN;
import java.util.regex.Pattern;

public class FormalDomainNameValidator implements DomainNameValidator {
    private static Pattern domainPattern;

    private static Pattern getLocalPartPattern() {
        if (domainPattern == null) {
            domainPattern = Pattern.compile(
                    "\\A((xn--[a-zA-Z0-9]|[a-zA-Z0-9])([a-zA-Z0-9]|-[a-zA-Z0-9])*)" +
                            "(\\.((xn--[a-zA-Z0-9]|[a-zA-Z0-9])([a-zA-Z0-9]|-[a-zA-Z0-9])*))*\\z"
            );
        }
        return domainPattern;
    }

    @Override
    public String getUniqueKey() {
        return "domain-name-format";
    }

    @Override
    public String getDescription() {
        return "This value must be a valid domain name.";
    }

    @Override
    public boolean isValid(@Nullable Object value) {
        if (!(value instanceof String)) {
            return false;
        }

        if (((String)value).contains("\n")) {
            return false;
        }

        try {
            return getLocalPartPattern().matcher(IDN.toASCII(((String)value))).matches();
        } catch (Exception e) {
            return false;
        }
    }
}
