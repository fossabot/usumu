package io.usumu.api.s3;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
public class S3Factory implements Supplier<AmazonS3> {
    private final S3Configuration s3Configuration;

    @Autowired
    public S3Factory(S3Configuration s3Configuration) {
        this.s3Configuration = s3Configuration;
    }

    public AmazonS3 get() {
        String region;
        if (s3Configuration.region != null) {
            region = s3Configuration.region;
        } else {
            region = Regions.DEFAULT_REGION.getName();
        }

        AmazonS3 client = getForRegion(region);
        try {
            String bucketLocation = client.getBucketLocation(s3Configuration.bucketName);
            if (!bucketLocation.equalsIgnoreCase(region)) {
                //Bucket is in a different region
                client = getForRegion(bucketLocation);
            }
        } catch (AmazonS3Exception s3Exception) {
            if (s3Exception.getErrorCode().equals("NoSuchBucket")) {
                client.createBucket(s3Configuration.bucketName);
            } else {
                throw new RuntimeException(s3Exception);
            }
        }

        return client;
    }

    private AmazonS3 getForRegion(String region) {
        AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard();
        builder.withCredentials(getCredentialsProvider());
        builder.withPathStyleAccessEnabled(true);

        if (s3Configuration.bucketHost != null && !s3Configuration.bucketHost.isEmpty()) {
            builder = builder.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
                String.format(s3Configuration.bucketHost, region),
                region
            ));
        } else {
            builder = builder.withRegion(region);
        }

        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.withProtocol(Protocol.HTTPS);
        //todo add proxy support
        builder.withClientConfiguration(clientConfiguration);

        return builder.build();
    }

    private AWSCredentialsProvider getCredentialsProvider() {
        if (s3Configuration.accessKeyId != null && !s3Configuration.accessKeyId.isEmpty() && s3Configuration.secretAccessKey != null && !s3Configuration.secretAccessKey.isEmpty()) {
            return new AWSCredentialsProvider() {
                @Override
                public AWSCredentials getCredentials() {
                    return new AWSCredentials() {
                        @Override
                        public String getAWSAccessKeyId() {
                            return s3Configuration.accessKeyId;
                        }

                        @Override
                        public String getAWSSecretKey() {
                            return s3Configuration.secretAccessKey;
                        }
                    };
                }

                @Override
                public void refresh() {

                }
            };
        } else {
            return new DefaultAWSCredentialsProviderChain();
        }
    }
}
