

export default interface Validator {
    getErrorMessage():string;
    getUniqueKey():string
    isValid(value?: any):boolean;
}