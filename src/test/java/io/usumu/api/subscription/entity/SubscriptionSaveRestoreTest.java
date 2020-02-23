package io.usumu.api.subscription.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.usumu.api.crypto.EntityCrypto;
import io.usumu.api.crypto.GlobalSecret;
import io.usumu.api.crypto.HashGenerator;
import io.usumu.api.crypto.SecretGenerator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SubscriptionSaveRestoreTest {
    @Test
    public void testSaveRestore() throws Throwable {
        GlobalSecret globalSecret = new GlobalSecret(
            "abcdefghijklmnopqrstuvwxyz123456".getBytes(),
            "abcdefghijklmnopqrstuvwxyz123456".getBytes()
        );
        Subscription subscription = new Subscription(
                SubscriptionMethod.EMAIL,
                "test@example.com",
                new HashGenerator(globalSecret),
                new SecretGenerator()
        );
        String originalVerificationCode = subscription.getVerificationCode(new HashGenerator(globalSecret));
        EncryptedSubscription encryptedSubscription = new EncryptedSubscription(
                subscription,
                new EntityCrypto(new ObjectMapper(), globalSecret)
        );

        Subscription decryptedSubscription = new EntityCrypto(new ObjectMapper(), globalSecret).decrypt(encryptedSubscription.encryptedData, Subscription.class);

        assertEquals(originalVerificationCode, decryptedSubscription.getVerificationCode(new HashGenerator(globalSecret)));
    }
}
