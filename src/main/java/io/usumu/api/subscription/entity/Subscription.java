package io.usumu.api.subscription.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.usumu.api.crypto.GlobalSecret;
import io.usumu.api.subscription.exception.VerificationFailed;
import org.springframework.lang.Nullable;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Subscription {
    public final String id;
    public final Type type;
    @Nullable
    public final String value;
    public final Status status;
    public final byte[] secret;

    public Subscription(
            Type type,
            String value,
            GlobalSecret globalSecret
    ) {
        this.id = computeHash(value, globalSecret.secret);
        this.type = type;
        this.value = value;
        this.status = Status.UNCONFIRMED;
        this.secret = createSecret();
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

    public String getVerificationCode() {
        return computeHash("confirmation", secret);
    }

    public Subscription verify(String verificationCode) throws VerificationFailed {
        if (verificationCode.equals(computeHash("confirmation", secret))) {
            //Rotate the secret
            return new Subscription(
                    id,
                    type,
                    value,
                    Status.CONFIRMED,
                    createSecret()
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

    public static String getIdFromValue(String value, GlobalSecret globalSecret) {
        return computeHash(value, globalSecret.secret);
    }

    private static byte[] createSecret() {
        byte[] secret = new byte[32];
        new SecureRandom().nextBytes(secret);
        return secret;
    }

    private static String computeHash(String data, byte[] secret){
        Mac sha512_HMAC;
        String result;

        try{
            final String HMAC_SHA512 = "HmacSHA512";
            sha512_HMAC = Mac.getInstance(HMAC_SHA512);
            SecretKeySpec keySpec = new SecretKeySpec(secret, HMAC_SHA512);
            sha512_HMAC.init(keySpec);
            byte [] mac_data = sha512_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
            result = bytesToHex(mac_data);
            return result;
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
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

    private static String bytesToHex(byte[] bytes) {
        final  char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
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
