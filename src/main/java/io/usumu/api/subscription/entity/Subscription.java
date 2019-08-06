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
     * The type (EMAIL or SMS) of the subscriber
     */
    @SuppressWarnings("WeakerAccess")
    public final Type type;
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
            Type type,
            String value,
            HashGenerator hashGenerator,
            SecretGenerator secretGenerator
    ) throws InvalidParameters {
        ValidatorChain validatorChain = new ValidatorChain();

        validatorChain.addValidator("type", new RequiredValidator());
        validatorChain.addValidator("value", new RequiredValidator());
        switch(type) {
            case EMAIL:
                validatorChain.addValidator("value", new FormalEmailValidator());
                break;
            case SMS:
                validatorChain.addValidator("value", new PhoneNumberValidator());
                break;
            default:
                if (type != null) {
                    throw new RuntimeException("Unsupported type: " + type);
                }
        }

        Map<String, Object> data = new HashMap<>();
        data.put("type", type);
        data.put("value", value);

        validatorChain.validate(data);

        this.id = hashGenerator.generateHash(value);
        this.type = type;
        this.value = value;
        this.status = Status.UNCONFIRMED;
        this.secret = secretGenerator.get();
    }

    @JsonCreator
    public Subscription(
        @JsonProperty("id")
            String id,
            @JsonProperty("type")
            Type type,
            @Nullable
            @JsonProperty("value")
            String value,
            @JsonProperty("status")
            Status status,
            @JsonProperty("secret")
            byte[] secret
    ) {
        this.id = id;
        this.type = type;
        this.value = value;
        this.status = status;
        this.secret = secret;
    }

    public String getVerificationCode(HashGenerator hashGenerator) {
        return hashGenerator.generateHash("confirmation", secret);
    }

    public Subscription verify(HashGenerator hashGenerator, SecretGenerator secretGenerator, String verificationCode) throws VerificationFailed {
        if (verificationCode.equals(hashGenerator.generateHash("confirmation", secret))) {
            //Rotate the secret
            return new Subscription(
                    id,
                    type,
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
                type,
                null,
                Status.UNSUBSCRIBED,
                secret
        );
    }

    public Subscription withSubscribeInitiated(String value, GlobalSecret globalSecret) {
        return new Subscription(
            id,
            type,
            value,
            Status.UNCONFIRMED,
            globalSecret.secret
        );
    }

    public Subscription withUnsubscribed() {
        return new Subscription(
            id,
            type,
            null,
            Status.UNSUBSCRIBED,
            new byte[0]
        );
    }

    public enum Type {
        EMAIL,
        SMS;

        public static Type fromString(String type) {
            for (Type value : Type.values()) {
                if (value.toString().equalsIgnoreCase(type)) {
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
