package io.usumu.api.crypto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class SecretGenerator {
    @Autowired
    public SecretGenerator() {
    }

    public byte[] get() {
        byte[] secret = new byte[32];
        new SecureRandom().nextBytes(secret);
        return secret;
    }
}
