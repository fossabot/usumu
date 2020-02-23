package io.usumu.api.template;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import io.usumu.api.s3.S3Accessor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

@Service
public class S3TemplateProvider implements TemplateProvider {
    private final S3Accessor s3Accessor;

    public S3TemplateProvider(final S3Accessor s3Accessor) {
        this.s3Accessor = s3Accessor;
    }

    @Override
    public String load(final String template) throws TemplateNotFound {
        try {
            final S3ObjectInputStream inputStream = s3Accessor.get("t/" + template)
                .getObjectContent();
            final int           bufferSize = 1024;
            final char[]        buffer     = new char[bufferSize];
            final StringBuilder out        = new StringBuilder();
            Reader              in         = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            int                 charsRead;
            while ((charsRead = in.read(buffer, 0, buffer.length)) > 0) {
                out.append(buffer, 0, charsRead);
            }
            return out.toString();
        } catch (S3Accessor.ObjectNotFoundException e) {
            throw new TemplateNotFound(template);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(final String file, final String content) {
        s3Accessor.put("t/" + file, new ByteArrayInputStream(content.getBytes()));
    }
}
