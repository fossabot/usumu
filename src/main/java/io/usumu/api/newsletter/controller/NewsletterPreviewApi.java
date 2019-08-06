package io.usumu.api.newsletter.controller;

import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(
        tags = "Newsletter"
)
public class NewsletterPreviewApi {
    @ApiOperation(
            nickname = "previewNewsletter",
            value = "Preview a newsletter",
            notes = "Preview a newsletter with a set of metadata",
            consumes = "application/json",
            produces = "application/json"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "The rendered newsletter.", response = NewsletterPreviewResponse.class),
            }
    )
    @RequestMapping(
            value = "/newsletters/{id}/preview/{value}",
            method = RequestMethod.GET
    )
    public NewsletterPreviewResponse preview(
            @ApiParam(
                    value = "Newsletter ID",
                    required = true
            )
            @PathVariable
            String id,
            @ApiParam(
                    value = "Subscriber ID, or subscriber info (EMAIL or PHONE in international number) to use for preview.",
                    required = true
            )
            @PathVariable
            String value
    ) {
        return null;
    }

    public static class NewsletterPreviewResponse {
        @SuppressWarnings({"WeakerAccess", "unused"})
        public final String subject;
        @SuppressWarnings({"WeakerAccess", "unused"})
        public final String text;
        @SuppressWarnings({"WeakerAccess", "unused"})
        public final String html;
        @SuppressWarnings({"WeakerAccess", "unused"})
        public final String sms;

        public NewsletterPreviewResponse(String subject, String text, String html, String sms) {
            this.subject = subject;
            this.text = text;
            this.html = html;
            this.sms = sms;
        }
    }
}
