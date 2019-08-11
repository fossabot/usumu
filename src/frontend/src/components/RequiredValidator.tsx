import Validator from "./Validator";
import {string} from "prop-types";

export default class RequiredValidator implements Validator {
    getErrorMessage(): string {
        return "This field is required.";
    }

    getUniqueKey(): string {
        return "required";
    }

    isValid(value?: any): boolean {
        if (value == null) {
            return false;
        }
        // noinspection RedundantIfStatementJS
        if (value instanceof string && value as string == "") {
            return false;
        }
        return true;
    }
}