package io.usumu.api.crypto;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.usumu.api.subscription.exception.DecryptionFailed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Service
public class EntityCrypto {
    private final ObjectMapper objectMapper;
    private final byte[] key;
    private final byte[] initVector;

    @Autowired
    public EntityCrypto(
        ObjectMapper objectMapper,
        GlobalSecret globalSecret
    ) {
        this.objectMapper = objectMapper;
        this.key = globalSecret.secret;

        byte[] initVector = new byte[16];
        System.arraycopy(globalSecret.initVector, 0, initVector, 0, 16);

        this.initVector = initVector;
    }

    public <T> String encrypt(T entity) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            objectMapper.writeValue(outputStream, entity);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        byte[] jsonData = outputStream.toByteArray();
        return encrypt(jsonData);
    }

    public <T> T decrypt(String data, Class<T> resultClass) throws DecryptionFailed {
        byte[] decodedData = decrypt(data);
        try {
            return objectMapper.readValue(decodedData, resultClass);
        } catch (IOException e) {
            throw new DecryptionFailed(e);
        }
    }

    private String encrypt(byte[] value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector);
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value);
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] decrypt(String encrypted) throws DecryptionFailed {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector);
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));

            return original;
        } catch (Exception e) {
            throw new DecryptionFailed(e);
        }
    }
}
