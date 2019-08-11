import Validator from "./Validator";

export class FieldError {
    constructor(
        public readonly errorCode: string,
        public readonly errorMessage: string
    ) {

    }
}

export class ValidationError extends Error {
    constructor(
        public readonly fieldErrors: Map<String, Array<FieldError>>
    ) {
        super("One or more fields failed validation. Please make sure that the data provided is correct.");
    }
}

export default class ValidatorChain {

    private validators: Map<String, Array<Validator>> = new Map<String, Array<Validator>>();

    public addValidator(field: string, validator: Validator):void {
        let validatorList = this.validators.get(field) || [];
        validatorList.push(validator);
        this.validators.set(field, validatorList);
    }

    public validate(data: Map<String, Object>): void {
        let errors = new Map<String, Array<FieldError>>();
        let self = this;
        data.forEach(function(value, key) {
           if (self.validators.has(key)) {
               let validators = self.validators.get(key);
               if (validators != null) {
                   validators.forEach(function (validator) {
                       if (!validator.isValid(value)) {
                           let fieldErrors = errors.get(key) || [];
                           fieldErrors.push(new FieldError(
                               validator.getUniqueKey(),
                               validator.getErrorMessage()
                           ))
                       }
                   });
               }
           }
        });

        if (errors.size != 0) {
            throw new ValidationError(errors);
        }
    }
}
