import Validator from "./Validator";
import {FieldError, ValidationError} from "./ValidatorChain";

export default class FieldValidatorChain {
    private validators: Array<Validator> = [];

    public addValidator(validator: Validator):void {
        this.validators.push(validator);
    }

    public validate(value?: Object): Array<FieldError> {
        let errors = Array<FieldError>();
        let self = this;

        let validators = self.validators;
        if (validators != null) {
            validators.forEach(function (validator) {
                if (!validator.isValid(value)) {
                    errors.push(new FieldError(
                        validator.getUniqueKey(),
                        validator.getErrorMessage()
                    ))
                }
            });
        }
        return errors;
    }

}