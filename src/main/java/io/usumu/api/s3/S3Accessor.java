package io.usumu.api.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class S3Accessor {
    private final S3Factory s3Factory;
    private final S3Configuration s3Configuration;

    public S3Accessor(final S3Factory s3Factory, final S3Configuration s3Configuration) {
        this.s3Factory = s3Factory;
        this.s3Configuration = s3Configuration;
    }

    public final S3Object get(String object) throws ObjectNotFoundException {
        try {
            return s3Factory
                .get()
                .getObject(s3Configuration.bucketName, object);
        } catch (AmazonS3Exception e) {
            if (e.getErrorCode().equalsIgnoreCase("NoSuchKey")) {
                throw new ObjectNotFoundException(object);
            } else {
                throw e;
            }
        }
    }

    public final void put(
        String key,
        InputStream data
    ) {
        s3Factory
            .get()
            .putObject(s3Configuration.bucketName, key, data, new ObjectMetadata());
    }

    public final ListObjectsV2Result list(
        String prefix,
        int maxItems,
        String continuationToken
    ) {
        ListObjectsV2Request request = new ListObjectsV2Request()
            .withBucketName(s3Configuration.bucketName)
            .withMaxKeys(maxItems)
            .withPrefix(prefix);
        if (continuationToken != null) {
            request = request.withContinuationToken(continuationToken);
        }
        AmazonS3 client = s3Factory.get();

        return client.listObjectsV2(request);
    }

    public static class ObjectNotFoundException extends Exception {
        public ObjectNotFoundException(String object) {
            super("Object not found in bucket: " + object);
        }
    }
}
