package io.usumu.api.subscription.storage;

import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.usumu.api.common.entity.PaginatedList;
import io.usumu.api.s3.S3Accessor;
import io.usumu.api.s3.S3Storage;
import io.usumu.api.subscription.entity.EncryptedSubscription;
import io.usumu.api.subscription.exception.SubscriptionNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class S3SubscriptionStorage implements SubscriptionStorageUpsert, SubscriptionStorageGet, SubscriptionStorageList {
    private final S3Storage s3Storage;

    @Autowired
    public S3SubscriptionStorage(
        S3Storage s3Storage
    ) {
        this.s3Storage = s3Storage;
    }

    @Override
    public EncryptedSubscription get(String hash) throws SubscriptionNotFound {
        try {
            return s3Storage.get("s/" + hash, EncryptedSubscription.class);
        } catch (S3Accessor.ObjectNotFoundException e) {
            throw new SubscriptionNotFound();
        }
    }

    @Override
    public PaginatedList<EncryptedSubscription> list(
        int maxItems,
        String continuationToken
    ) {
        return s3Storage.list("s/", maxItems, continuationToken, EncryptedSubscription.class);
    }

    @Override
    public void store(EncryptedSubscription encryptedSubscription) {
        s3Storage.store(encryptedSubscription, "s/" + encryptedSubscription.hash);
    }
}
