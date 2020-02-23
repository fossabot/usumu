package io.usumu.api.subscription.storage;

import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.usumu.api.common.entity.PaginatedList;
import io.usumu.api.crypto.GlobalSecret;
import io.usumu.api.crypto.HashGenerator;
import io.usumu.api.s3.S3Accessor;
import io.usumu.api.s3.S3Configuration;
import io.usumu.api.s3.S3Factory;
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
public class S3Storage implements SubscriptionStorageUpsert, SubscriptionStorageGet, SubscriptionStorageList {
    private final S3Accessor      s3Accessor;
    private final ObjectMapper    objectMapper;

    @Autowired
    public S3Storage(
        final S3Accessor s3Accessor,
        ObjectMapper objectMapper
    ) {
        this.s3Accessor = s3Accessor;
        this.objectMapper = objectMapper;
    }

    @Override
    public EncryptedSubscription get(String hash) throws SubscriptionNotFound {
        S3Object object;
        try {
            object = s3Accessor.get("s/" + hash);
        } catch (S3Accessor.ObjectNotFoundException e) {
            throw new SubscriptionNotFound();
        }
        S3ObjectInputStream data = object.getObjectContent();

        try {
            return objectMapper.readValue(data, EncryptedSubscription.class);
        } catch (IOException e) {
            //todo handle properly
            throw new RuntimeException(e);
        }
    }

    @Override
    public PaginatedList<EncryptedSubscription> list(
        int maxItems,
        String continuationToken
    ) {
        ListObjectsV2Result listResponse = s3Accessor.list("s/", maxItems, continuationToken);

        return new PaginatedList<>(
            listResponse.getContinuationToken(),
            listResponse
                .getObjectSummaries()
                .stream()
                .map(summary -> {
                    try {
                        return s3Accessor.get(summary.getKey()).getObjectContent();
                    } catch (S3Accessor.ObjectNotFoundException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .map(bucketContent -> {
                    try {
                        return objectMapper.readValue(bucketContent, EncryptedSubscription.class);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList())
        );
    }

    @Override
    public void store(EncryptedSubscription encryptedSubscription) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            objectMapper.writeValue(outputStream, encryptedSubscription);
        } catch (IOException e) {
            //todo handle properly? Is this even an issue?
            throw new RuntimeException(e);
        }

        s3Accessor.put("s/" + encryptedSubscription.hash, new ByteArrayInputStream(outputStream.toByteArray()));
    }
}
