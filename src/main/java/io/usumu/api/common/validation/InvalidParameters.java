package io.usumu.api.common.validation;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.usumu.api.common.entity.ErrorCode;
import io.usumu.api.common.exception.ApiException;
import org.springframework.http.HttpStatus;

import java.util.Collection;
import java.util.Map;

public class InvalidParameters extends ApiException {
    @SuppressWarnings("WeakerAccess")
    @JsonProperty("fieldErrors")
    public final Map<String, Collection<Error>> fieldErrors;

    InvalidParameters(
        Map<String, Collection<Error>> fieldErrors
    ) {
        super(
            HttpStatus.BAD_REQUEST,
            ErrorCode.INVALID_PARAMETERS,
            "One or more request parameters are invalid. Please check the 'fieldErrors' field for details."
        );
        this.fieldErrors = fieldErrors;
    }
}
