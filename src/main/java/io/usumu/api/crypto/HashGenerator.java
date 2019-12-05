package io.usumu.api.crypto;

import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class HashGenerator {
    private final GlobalSecret globalSecret;

    public HashGenerator(GlobalSecret globalSecret) {

        this.globalSecret = globalSecret;
    }
    public String generateHash(String value, byte[] secret) {
        Mac sha512_HMAC;
        String result;

        try{
            final String HMAC_SHA512 = "HmacSHA512";
            sha512_HMAC = Mac.getInstance(HMAC_SHA512);
            SecretKeySpec keySpec = new SecretKeySpec(secret, HMAC_SHA512);
            sha512_HMAC.init(keySpec);
            byte [] mac_data = sha512_HMAC.doFinal(value.getBytes(StandardCharsets.UTF_8));
            result = bytesToHex(mac_data);
            return result;
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateHash(String value) {
        return generateHash(value, globalSecret.secret);
    }

    private String bytesToHex(byte[] bytes) {
        final  char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
