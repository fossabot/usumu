package io.usumu.api.crypto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class GlobalSecret {
    public final byte[] secret;
    public final byte[] initVector;

    @Autowired
    public GlobalSecret(
        @Value("${USUMU_SECRET}")
        byte[] globalSecret,
        @Value("${USUMU_INIT_VECTOR")
        byte[] initVector
    ) {
        this.initVector = initVector;
        Objects.requireNonNull(globalSecret);
        if (globalSecret.length < 32) {
            throw new RuntimeException("The USUMU_SECRET variable must be at least 32 bytes long!");
        }
        this.secret = globalSecret;
    }
}
