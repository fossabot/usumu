package io.usumu.api.subscription.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.usumu.api.common.validation.*;
import io.usumu.api.crypto.GlobalSecret;
import io.usumu.api.crypto.HashGenerator;
import io.usumu.api.crypto.SecretGenerator;
import io.usumu.api.subscription.exception.VerificationFailed;
import org.springframework.lang.Nullable;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

public class Subscription {
    /**
     * Public ID of this subscription.
     */
    @SuppressWarnings("WeakerAccess")
    public final String id;
    /**
     * The entryType (EMAIL or SMS) of the subscriber
     */
    @SuppressWarnings("WeakerAccess")
    public final Method method;
    /**
     * The e-mail address or phone number of the subscriber.
     */
    @Nullable
    @SuppressWarnings("WeakerAccess")
    public final String value;
    /**
     * Subscription status.
     */
    @SuppressWarnings("WeakerAccess")
    public final Status status;
    /**
     * Secret used for generating verification codes.
     */
    @SuppressWarnings("WeakerAccess")
    public final byte[] secret;

    public Subscription(
            Method method,
            String value,
            HashGenerator hashGenerator,
            SecretGenerator secretGenerator
    ) throws InvalidParameters {
        ValidatorChain validatorChain = new ValidatorChain();

        validatorChain.addValidator("entryType", new RequiredValidator());
        validatorChain.addValidator("value", new RequiredValidator());
        switch(method) {
            case EMAIL:
                validatorChain.addValidator("value", new FormalEmailValidator());
                break;
            case SMS:
                validatorChain.addValidator("value", new PhoneNumberValidator());
                break;
            default:
                if (method != null) {
                    throw new RuntimeException("Unsupported entryType: " + method);
                }
        }

        Map<String, Object> data = new HashMap<>();
        data.put("method", method);
        data.put("value", value);

        validatorChain.validate(data);

        this.id = hashGenerator.generateHash(value);
        this.method = method;
        this.value = value;
        this.status = Status.UNCONFIRMED;
        this.secret = secretGenerator.get();
    }

    @JsonCreator
    public Subscription(
        @JsonProperty("id")
            String id,
            @JsonProperty("method")
            Method method,
            @Nullable
            @JsonProperty("value")
            String value,
            @JsonProperty("status")
            Status status,
            @JsonProperty("secret")
            byte[] secret
    ) {
        this.id = id;
        this.method = method;
        this.value = value;
        this.status = status;
        this.secret = secret;
    }

    public String getVerificationCode(HashGenerator hashGenerator) {
        if (secret.length == 0) {
            return null;
        }
        return hashGenerator.generateHash("confirmation", secret).substring(0,8);
    }

    public Subscription verify(HashGenerator hashGenerator, SecretGenerator secretGenerator, String verificationCode) throws VerificationFailed {
        if (verificationCode.equals(getVerificationCode(hashGenerator))) {
            //Rotate the secret
            return new Subscription(
                    id,
                    method,
                    value,
                    Status.CONFIRMED,
                    secretGenerator.get()
            );
        }
        throw new VerificationFailed();
    }

    public Subscription unsubscribe() {
        return new Subscription(
                id,
                method,
                null,
                Status.UNSUBSCRIBED,
                secret
        );
    }

    public Subscription withSubscribeInitiated(String value, GlobalSecret globalSecret) {
        return new Subscription(
            id,
            method,
            value,
            Status.UNCONFIRMED,
            globalSecret.secret
        );
    }

    public Subscription withUnsubscribed() {
        return new Subscription(
            id,
            method,
            null,
            Status.UNSUBSCRIBED,
            new byte[0]
        );
    }

    public enum Method {
        EMAIL,
        SMS;

        public static Method fromString(String method) {
            for (Method value : Method.values()) {
                if (value.toString().equalsIgnoreCase(method)) {
                    return value;
                }
            }
            throw new InvalidParameterException();
        }
    }

    public enum Status {
        UNCONFIRMED,
        CONFIRMED,
        UNSUBSCRIBED;

        public static Status fromString(String status) {
            for (Status value : Status.values()) {
                if (value.toString().equalsIgnoreCase(status)) {
                    return value;
                }
            }
            throw new InvalidParameterException();
        }
    }
}
