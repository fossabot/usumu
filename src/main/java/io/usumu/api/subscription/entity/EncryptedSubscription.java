package io.usumu.api.subscription.entity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@ParametersAreNonnullByDefault
public class EncryptedSubscription {
    public final String hash;
    public final String encryptedData;

    public EncryptedSubscription(
            Subscription subscription,
            byte[] globalSecret,
            byte[] initVector
    ) {
        JsonObject storageObject = new JsonObject();
        storageObject.addProperty("id", subscription.id);
        storageObject.addProperty("type", subscription.type.toString());
        storageObject.addProperty("value", subscription.value);
        storageObject.addProperty("secret", Base64.getEncoder().encodeToString(subscription.secret));
        storageObject.addProperty("status", subscription.status.toString());

        hash = subscription.id;
        encryptedData = encrypt(globalSecret, initVector, storageObject.getAsString());
    }

    private EncryptedSubscription(
            String hash,
            String encryptedData
    ) {
        this.hash = hash;
        this.encryptedData = encryptedData;
    }

    public Subscription decrypt(String encryptedData, byte[] globalSecret, byte[] initVector) throws DecryptionFailed {
        String jsonString = decrypt(globalSecret, initVector, encryptedData);
        try {
            JsonObject jsonObject = new Gson().fromJson(jsonString, JsonObject.class);

            return new Subscription(
                    jsonObject.get("id").getAsString(),
                    Subscription.Type.fromString(jsonObject.get("type").getAsString()),
                    jsonObject.get("value").getAsString(),
                    Subscription.Status.fromString(jsonObject.get("status").getAsString()),
                    Base64.getDecoder().decode(jsonObject.get("secret").getAsString().getBytes())
            );
        } catch (Exception e) {
            throw new DecryptionFailed(e);
        }
    }

    private static String encrypt(byte[] key, byte[] initVector, String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector);
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String decrypt(byte[] key, byte[] initVector, String encrypted) throws DecryptionFailed {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector);
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));

            return new String(original);
        } catch (Exception e) {
            throw new DecryptionFailed(e);
        }
    }

    public static class DecryptionFailed extends Exception {
        public DecryptionFailed(Exception e) {
            super(e);
        }
    }
}