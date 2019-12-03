package io.usumu.api.common.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ResponseHeader;
import io.usumu.api.common.entity.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ResponseStatus;
import zone.refactor.spring.hateoas.entity.ExceptionEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unused")
public class ApiException extends ExceptionEntity {

    @JsonIgnore
    private final HttpStatus httpStatus;
    @JsonIgnore
    private final MultiValueMap<String, String> headers;

    @SuppressWarnings("WeakerAccess")
    public final ErrorCode errorCode;
    @SuppressWarnings("WeakerAccess")
    public final String errorMessage;

    public final Map<String, Set<String>> validationErrors;


    public ApiException(
        HttpStatus httpStatus,
        ErrorCode errorCode,
        String errorMessage
    ) {
        this(
            httpStatus,
            errorCode,
            errorMessage,
            new LinkedMultiValueMap<>()
        );
    }

    @SuppressWarnings("WeakerAccess")
    public ApiException(
        HttpStatus httpStatus,
        ErrorCode errorCode,
        String errorMessage,
        MultiValueMap<String, String> headers
    ) {
        this(
            httpStatus,errorCode,errorMessage,headers,new HashMap<>()
        );
    }


    @SuppressWarnings("WeakerAccess")
    public ApiException(
        HttpStatus httpStatus,
        ErrorCode errorCode,
        String errorMessage,
        MultiValueMap<String, String> headers,
        Map<String, Set<String>> validationErrors
    ) {
        super();
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.headers = headers;
        this.validationErrors = validationErrors;
    }

    public ApiException(final HttpStatus httpStatus, final ErrorCode errorCode, final String errorMessage, final Map<String, Set<String>> validationErrors) {
        this(
            httpStatus,
            errorCode,
            errorMessage,
            new LinkedMultiValueMap<>(),
            validationErrors
        );
    }

    @JsonIgnore
    @ResponseStatus
    public HttpStatus getStatus() {
        return httpStatus;
    }

    @JsonIgnore
    @ResponseHeader
    public MultiValueMap<String, String> getHeaders() {
        return headers;
    }
}
