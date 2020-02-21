package io.usumu.api.template;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nullable;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping(
    value = "/templates"
)
public class TemplateUploadApi {
    private final TemplateProvider templateProvider;

    public TemplateUploadApi(final TemplateProvider templateProvider) {
        this.templateProvider = templateProvider;
    }

    @RequestMapping(value = {"/{filename}", "/{directory}/{filename}"})
    @ApiOperation(
        value = "Upload template file",
        nickname = "uploadFile"
    )
    public void upload(
        @Nullable
        @PathParam(value = "directory")
        String directory,
        @PathParam("filename")
        String filename,
        @RequestBody
        InputStream body
    ) {
        final String file = directory == null ? filename : directory + "/" + filename;

        final int           bufferSize = 1024;
        final char[]        buffer     = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        try {
            Reader              in  = new InputStreamReader(body, StandardCharsets.UTF_8);
            int                 charsRead;
            while ((charsRead = in.read(buffer, 0, buffer.length)) > 0) {
                out.append(buffer, 0, charsRead);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        templateProvider.save(file, out.toString());
    }
}
