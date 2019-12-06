package io.usumu.api.subscription.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.usumu.api.common.validation.*;
import io.usumu.api.crypto.GlobalSecret;
import io.usumu.api.crypto.HashGenerator;
import io.usumu.api.crypto.SecretGenerator;
import io.usumu.api.subscription.exception.VerificationFailed;
import org.springframework.lang.Nullable;

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
    public final SubscriptionMethod method;
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
    public final SubscriptionStatus status;
    /**
     * Secret used for generating verification codes.
     */
    @SuppressWarnings("WeakerAccess")
    public final byte[] secret;

    public Subscription(
            SubscriptionMethod method,
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
        this.status = SubscriptionStatus.UNCONFIRMED;
        this.secret = secretGenerator.get();
    }

    @JsonCreator
    public Subscription(
        @JsonProperty("id")
            String id,
            @JsonProperty("method")
            SubscriptionMethod method,
            @Nullable
            @JsonProperty("value")
            String value,
            @JsonProperty("status")
            SubscriptionStatus status,
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
                SubscriptionStatus.CONFIRMED,
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
            SubscriptionStatus.UNSUBSCRIBED,
            secret
        );
    }

    public Subscription withSubscribeInitiated(String value, GlobalSecret globalSecret) {
        return new Subscription(
            id,
            method,
            value,
            SubscriptionStatus.UNCONFIRMED,
            globalSecret.secret
        );
    }

    public Subscription withUnsubscribed() {
        return new Subscription(
            id,
            method,
            null,
            SubscriptionStatus.UNSUBSCRIBED,
            new byte[0]
        );
    }

}
