package io.usumu.api.common.exception;

import io.usumu.api.common.entity.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import zone.refactor.spring.validation.chain.ExceptionFactory;

import java.util.Map;
import java.util.Set;

@Service
public class ApiExceptionFactory implements ExceptionFactory<ApiException> {
    @Override
    public void create(final Map<String, Set<String>> errors) throws ApiException {
        throw new ApiException(
            HttpStatus.BAD_REQUEST,
            ErrorCode.INVALID_PARAMETERS,
            "Please check your details and try again.",
            errors
        );
    }
}
