package io.usumu.api.common.validation;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

public class PhoneNumberValidator implements Validator {
    @Override
    public String getUniqueKey() {
        return "phone-number";
    }

    @Override
    public String getDescription() {
        return "Invalid phone number. Please provide the phone number in international format (+12345678)";
    }

    @Override
    public boolean isValid(Object value) {
        if (value == null) {
            return true;
        }
        if (!(value instanceof String)) {
            return false;
        }

        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber phoneNumber = null;
        try {
            phoneNumber = phoneUtil.parse((CharSequence) value, "US");
        } catch (NumberParseException e) {
            return false;
        }

        return phoneUtil.isValidNumber(phoneNumber);
    }
}
