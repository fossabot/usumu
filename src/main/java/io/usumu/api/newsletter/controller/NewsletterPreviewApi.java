package io.usumu.api.newsletter.controller;

import io.swagger.annotations.*;
import io.usumu.api.common.entity.ApiError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
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
                    @ApiResponse(code = 404, message = "The newsletter was not found.", response = ApiError.class)
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
        public final String subject;
        public final String text;
        public final String html;
        public final String sms;

        public NewsletterPreviewResponse(String subject, String text, String html, String sms) {
            this.subject = subject;
            this.text = text;
            this.html = html;
            this.sms = sms;
        }
    }
}
