package io.usumu.api.common.validation;

import io.usumu.api.common.entity.ApiError;
import io.usumu.api.common.exception.ApiException;
import org.springframework.http.HttpStatus;

import java.util.Collection;
import java.util.Map;

public class InvalidParameters extends ApiException {
    public final Map<String, Collection<Error>> fieldErrors;

    public InvalidParameters(
        Map<String, Collection<Error>> fieldErrors
    ) {
        super(
            HttpStatus.BAD_REQUEST,
            ApiError.ErrorCode.INVALID_PARAMETERS,
            "One or more request parameters are invalid. Please check the 'fieldErrors' field for details."
        );
        this.fieldErrors = fieldErrors;
    }
}
