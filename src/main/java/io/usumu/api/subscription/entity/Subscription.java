package io.usumu.api.subscription.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.usumu.api.common.validation.*;
import io.usumu.api.crypto.GlobalSecret;
import io.usumu.api.crypto.HashGenerator;
import io.usumu.api.crypto.SecretGenerator;
import io.usumu.api.subscription.exception.SubscriptionAlreadyVerified;
import io.usumu.api.subscription.exception.SubscriptionDeleted;
import io.usumu.api.subscription.exception.VerificationFailed;
import org.springframework.lang.Nullable;

import java.util.HashMap;
import java.util.Map;

public class Subscription {
    /**
     * Public ID of this subscription.
     */
    @SuppressWarnings("WeakerAccess")
    @JsonProperty("id")
    public final String id;
    /**
     * The entryType (EMAIL or SMS) of the subscriber
     */
    @SuppressWarnings("WeakerAccess")
    @JsonProperty("method")
    public final SubscriptionMethod method;
    /**
     * The e-mail address or phone number of the subscriber.
     */
    @Nullable
    @SuppressWarnings("WeakerAccess")
    @JsonProperty("value")
    public final String value;
    /**
     * Subscription status.
     */
    @SuppressWarnings("WeakerAccess")
    @JsonProperty("status")
    public final SubscriptionStatus status;
    /**
     * Secret used for generating verification codes.
     */
    @SuppressWarnings("WeakerAccess")
    @JsonIgnore
    public final byte[] secret;

    @JsonProperty("secret")
    public String getSecretAsHex() {
        final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[secret.length * 2];
        for (int j = 0; j < secret.length; j++) {
            int v = secret[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    public Subscription(
            SubscriptionMethod method,
            String value,
            HashGenerator hashGenerator,
            SecretGenerator secretGenerator
    ) throws InvalidParameters {
        this(
            method,
            value,
            SubscriptionStatus.UNCONFIRMED,
            hashGenerator,
            secretGenerator
        );
    }

    public Subscription(
        final SubscriptionMethod method,
        final String value,
        final SubscriptionStatus importStatus,
        final HashGenerator hashGenerator,
        final SecretGenerator secretGenerator
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
        data.put("status", importStatus);

        validatorChain.validate(data);

        this.id = hashGenerator.generateHash(value);
        this.method = method;
        if (importStatus == SubscriptionStatus.UNSUBSCRIBED) {
            this.value = value;
        } else {
            this.value = null;
        }
        this.status = importStatus;
        if (importStatus == SubscriptionStatus.UNCONFIRMED || importStatus == null) {
            this.secret = secretGenerator.get();
        } else {
            this.secret = null;
        }
    }

    public Subscription(
        String id,
        SubscriptionMethod method,
        @Nullable
        String value,
        SubscriptionStatus status,
        byte[] secret
    ) {
        this.id = id;
        this.method = method;
        this.value = value;
        this.status = status;
        this.secret = secret;
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
                    String secretHex
    ) {
        this.id = id;
        this.method = method;
        this.value = value;
        this.status = status;

        int len = secretHex.length();
        byte[] secret = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            secret[i / 2] = (byte) ((Character.digit(secretHex.charAt(i), 16) << 4)
                    + Character.digit(secretHex.charAt(i+1), 16));
        }
        this.secret = secret;
    }

    public String getVerificationCode(HashGenerator hashGenerator) {
        if (secret.length == 0) {
            return null;
        }
        return hashGenerator.generateHash("confirmation", secret).substring(0,8);
    }

    public Subscription verify(HashGenerator hashGenerator, SecretGenerator secretGenerator, String verificationCode)
        throws VerificationFailed, SubscriptionAlreadyVerified, SubscriptionDeleted {
        if (this.status == SubscriptionStatus.CONFIRMED) {
            throw new SubscriptionAlreadyVerified();
        } else if (this.status == SubscriptionStatus.UNSUBSCRIBED) {
            throw new SubscriptionDeleted();
        }
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
