package io.usumu.api.s3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class S3Configuration {
    public final String accessKeyId;
    public final String secretAccessKey;
    public final String region;
    @Nullable
    public final String endpoint;
    public final String bucketName;
    @Nullable
    public final String bucketHost;

    public S3Configuration(
        @Nullable
        @Value("${USUMU_S3_ACCESS_KEY_ID:#{null}}")
        String accessKeyId,
        @Nullable
        @Value("${USUMU_S3_SECRET_ACCESS_KEY:#{null}}")
        String secretAccessKey,
        @Nullable
        @Value("${USUMU_S3_REGION:#{null}}")
        String region,
        @Nullable
        @Value("${USUMU_S3_ENDPOINT:#{null}}")
        String endpoint,
        @Value("${USUMU_S3_BUCKET}")
        String bucketName,
        @Nullable
        @Value("${USUMU_S3_BUCKET_HOST:#{null}}")
        String bucketHost
    ) {
        this.accessKeyId = accessKeyId;
        this.secretAccessKey = secretAccessKey;
        this.region = region;
        this.endpoint = endpoint;
        this.bucketName = bucketName;
        this.bucketHost = bucketHost;
    }
}
