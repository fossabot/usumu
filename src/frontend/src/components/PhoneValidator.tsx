import Validator from "./Validator";
import {string} from "prop-types";

export default class PhoneValidator implements Validator {
    getErrorMessage(): string {
        return "The specified phone number is not valid.";
    }

    getUniqueKey(): string {
        return "phone";
    }

    isValid(value?: any): boolean {
        if (value == null) {
            return true;
        }
        if (!(value instanceof string)) {
            return true;
        }
        return true;
    }

}