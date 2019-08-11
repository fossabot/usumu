import Validator from "./Validator";
import {string} from "prop-types";

export default class FormalDomainNameValidator implements Validator {
    private readonly pattern: RegExp = new RegExp('\\A((xn--[a-zA-Z0-9]|[a-zA-Z0-9])([a-zA-Z0-9]|-[a-zA-Z0-9])*)(\\.((xn--[a-zA-Z0-9]|[a-zA-Z0-9])([a-zA-Z0-9]|-[a-zA-Z0-9])*))*\\z');

    getErrorMessage(): string {
        return "The specified domain name is not valid.";
    }

    getUniqueKey(): string {
        return "domain-name";
    }

    isValid(value?: any): boolean {
        if (value == null) {
            return true;
        }
        if (!(value instanceof string)) {
            return true;
        }
        let stringValue = value as string;
        return this.pattern.test(stringValue);
    }
}