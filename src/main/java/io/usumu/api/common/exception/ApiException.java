package io.usumu.api.common.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import io.usumu.api.common.entity.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

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

    public final ApiError.ErrorCode errorCode;
    public final String errorMessage;

    public ApiException(
        HttpStatus httpStatus,
        ApiError.ErrorCode errorCode,
        String errorMessage
    ) {
        this(
            httpStatus,
            errorCode,
            errorMessage,
            new LinkedMultiValueMap<>()
        );
    }

    public ApiException(
        HttpStatus httpStatus,
        ApiError.ErrorCode errorCode,
        String errorMessage,
        MultiValueMap<String, String> headers
    ) {
        super();
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.headers = headers;
    }
}
