package io.usumu.api.s3;

import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.usumu.api.common.entity.PaginatedList;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class S3Storage {
    private final S3Accessor s3Accessor;
    private final ObjectMapper objectMapper;

    public S3Storage(S3Accessor s3Accessor, ObjectMapper objectMapper) {
        this.s3Accessor = s3Accessor;
        this.objectMapper = objectMapper;
    }

    public <T> T get(String path, Class<T> entityClass) throws S3Accessor.ObjectNotFoundException {
        S3Object object;
        object = s3Accessor.get(path);
        S3ObjectInputStream data = object.getObjectContent();

        try {
            return objectMapper.readValue(data, entityClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> PaginatedList<T> list(
            String prefix,
            int maxItems,
            String continuationToken,
            Class<T> entityClass
    ) {
        ListObjectsV2Result listResponse = s3Accessor.list(prefix, maxItems, continuationToken);

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
                                return objectMapper.readValue(bucketContent, entityClass);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .collect(Collectors.toList())
        );
    }

    public <T> void store(T entity, String prefix) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            objectMapper.writeValue(outputStream, entity);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        s3Accessor.put(prefix, new ByteArrayInputStream(outputStream.toByteArray()));
    }
}
