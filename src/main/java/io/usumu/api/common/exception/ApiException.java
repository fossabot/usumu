package io.usumu.api.common.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import io.usumu.api.common.entity.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unused")
@JsonIgnoreProperties({"detailMessage", "cause", "stackTrace", "suppressedExceptions", "backtrace", "message",
    "localizedMessage", "suppressed", "httpStatus", "headers"})
public class ApiException extends Exception {
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private String detailMessage;
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private String localizedMessage;
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private String message;
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private Throwable cause;
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private StackTraceElement[] stackTrace;
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private List<Throwable> suppressedExceptions;
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private List<?> suppressed;

    @JsonIgnore
    public final HttpStatus httpStatus;
    @JsonIgnore
    public final MultiValueMap<String, String> headers;

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
}
