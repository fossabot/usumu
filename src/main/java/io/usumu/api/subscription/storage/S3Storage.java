package io.usumu.api.subscription.storage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.usumu.api.common.entity.PaginatedList;
import io.usumu.api.crypto.GlobalSecret;
import io.usumu.api.crypto.HashGenerator;
import io.usumu.api.s3.S3Configuration;
import io.usumu.api.s3.S3Factory;
import io.usumu.api.subscription.entity.EncryptedSubscription;
import io.usumu.api.subscription.exception.SubscriptionNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.stream.Collectors;

@Service
public class S3Storage implements SubscriptionStorageUpsert, SubscriptionStorageGet, SubscriptionStorageList {

    private final S3Factory s3Factory;
    private final S3Configuration s3Configuration;
    private final ObjectMapper objectMapper;
    private final GlobalSecret globalSecret;
    private final HashGenerator hashGenerator;

    @Autowired
    public S3Storage(
        S3Factory s3Factory,
        S3Configuration s3Configuration,
        ObjectMapper objectMapper,
        GlobalSecret globalSecret,
        HashGenerator hashGenerator
    ) {
        this.s3Factory = s3Factory;
        this.s3Configuration = s3Configuration;
        this.objectMapper = objectMapper;
        this.globalSecret = globalSecret;
        this.hashGenerator = hashGenerator;
    }

    @Override
    public EncryptedSubscription get(String hash) throws SubscriptionNotFound {
        S3Object object;
        try {
            object = s3Factory
                .get()
                .getObject(s3Configuration.bucketName, "s/" + hash);
        } catch (AmazonS3Exception e) {
            if (!e.getErrorCode().equalsIgnoreCase("NoSuchKey")) {
                throw e;
            }
            try {
                object = s3Factory
                    .get()
                    .getObject(s3Configuration.bucketName, "s/" + hashGenerator.generateHash(hash));
            } catch (AmazonS3Exception e2) {
                if (!e2.getErrorCode().equalsIgnoreCase("NoSuchKey")) {
                    throw e2;
                }
                throw new SubscriptionNotFound();
            }
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
        ListObjectsV2Request request = new ListObjectsV2Request()
            .withBucketName(s3Configuration.bucketName)
            .withMaxKeys(maxItems);
        if (continuationToken != null) {
            request = request.withContinuationToken(continuationToken);
        }

        AmazonS3 client = s3Factory.get();

        ListObjectsV2Result listResponse = client.listObjectsV2(request);

        return new PaginatedList<>(
            listResponse.getContinuationToken(),
            listResponse
                .getObjectSummaries()
                .stream()
                .map(summary -> client.getObject(s3Configuration.bucketName, summary.getKey()).getObjectContent())
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
            //todo handle properly?
            throw new RuntimeException(e);
        }

        //todo handle bucket name null
        s3Factory
            .get()
            .putObject(s3Configuration.bucketName, "s/" + encryptedSubscription.hash, outputStream.toString());
    }
}
