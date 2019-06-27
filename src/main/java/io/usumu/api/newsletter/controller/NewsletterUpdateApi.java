package io.usumu.api.newsletter.controller;

import io.swagger.annotations.*;
import io.usumu.api.common.entity.ApiError;
import io.usumu.api.newsletter.entity.Newsletter;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@RestController
@Api(
        tags = "Newsletter"
)
public class NewsletterUpdateApi {
    @ApiOperation(
            nickname = "updateNewsletter",
            value = "Update/send a newsletter.",
            notes = "Update or send. Templates can be supplied in Thymeleaf format. Sending can be triggered using the 'send' parameter.",
            consumes = "application/json",
            produces = "application/json"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "A newsletter successfully updated.", response = NewsletterCreateResponse.class),
                    @ApiResponse(code = 404, message = "The newsletter was not found.", response = ApiError.class)
            }
    )
    @RequestMapping(
            value = "/newsletters/{id}",
            method = RequestMethod.PATCH
    )
    public NewsletterCreateResponse update(
            @ApiParam(
                    value = "Id of the newsletter",
                    required = true
            )
            @PathVariable
                    String id,

            @ApiParam(
                    value = "Subject template for the e-mail",
                    required = false
            )
            @RequestParam
            @Nullable
                    String subject,

            @ApiParam(
                    value = "Template for the text part of the e-mail",
                    required = false
            )
            @RequestParam
            @Nullable
                    String textTemplate,

            @ApiParam(
                    value = "Template for the HTML part of the e-mail",
                    required = false
            )
            @RequestParam
            @Nullable
                    String htmlTemplate,

            @ApiParam(
                    value = "Template for SMS",
                    required = false
            )
            @RequestParam
            @Nullable
                    String smsTemplate,

            @ApiParam(
                    value = "Only send out newsletter to subscribers who have this metadata key. Note that this can only be changed while the newsletter is in the NEW status.",
                    required = false
            )
            @RequestParam
                    String filterKey,

            @ApiParam(
                    value = "Only send out newsletter to subscribers who have this metadata value for the key specified in filterKey. Note that this can only be changed while the newsletter is in the NEW status.",
                    required = false
            )
            @RequestParam
                    String filterValue,

            @ApiParam(
                    value = "Set to true to start the sending process. Set to false to pause a sending. While sending the templates cannot be updated.",
                    required = false
            )
            @RequestParam
            @Nullable
                    Boolean send
    ) {
        return null;
    }

    public static class NewsletterCreateResponse {
        public final Newsletter newsletter;

        public NewsletterCreateResponse(Newsletter newsletter) {
            this.newsletter = newsletter;
        }
    }
}
