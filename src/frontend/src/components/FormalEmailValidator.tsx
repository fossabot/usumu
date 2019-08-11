import Validator from "./Validator";
import {string} from "prop-types";
import FormalDomainNameValidator from "./FormalDomainNameValidator";

export default class FormalEmailValidator implements Validator {
    private readonly localPattern: RegExp = new RegExp('^[a-zA-Z0-9-_+.\u007F-\uFFFF]+$');
    private readonly domainValidator: FormalDomainNameValidator = new FormalDomainNameValidator();

    getErrorMessage(): string {
        return "The provided e-mail address is not valid.";
    }

    getUniqueKey(): string {
        return "email";
    }

    isValid(value?: any): boolean {
        if (value == null) {
            return true;
        }
        if (!(value instanceof string)) {
            return true;
        }
        let valueString = value as string;
        let valueParts = valueString.split("@", 2);
        if (valueParts.length != 2) {
            return false;
        }
        let localPart = valueParts[0];
        let domainName = valueParts[1];

        if (!this.localPattern.test(localPart)) {
            return false;
        }
        if (!this.domainValidator.isValid(domainName)) {
            return false;
        }
        return true;
    }
}