package io.usumu.api.newsletter.controller;

import io.swagger.annotations.*;
import io.usumu.api.newsletter.entity.Newsletter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(
        tags = "Newsletter"
)
public class NewsletterCreateApi {
    @ApiOperation(
            nickname = "createNewsletter",
            value = "Create a new newsletter.",
            notes = "Create a new newsletter by providing templates in Thymeleaf format. Subscriber metadata can be accessed as variables.",
            consumes = "application/json",
            produces = "application/json"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "A newsletter successfully created.", response = NewsletterCreateResponse.class),
            }
    )
    @RequestMapping(
            value = "/newsletters",
            method = RequestMethod.POST
    )
    public NewsletterCreateResponse create(
            @ApiParam(
                    value = "Subject template for the e-mail",
                    required = true
            )
            @RequestParam
                    String subject,

            @ApiParam(
                    value = "Template for the text part of the e-mail",
                    required = true
            )
            @RequestParam
                    String textTemplate,

            @ApiParam(
                    value = "Template for the HTML part of the e-mail",
                    required = true
            )
            @RequestParam
                    String htmlTemplate,

            @ApiParam(
                    value = "Template for SMS",
                    required = true
            )
            @RequestParam
                    String smsTemplate,

            @ApiParam(
                    value = "Only send out newsletter to subscribers who have this metadata key.",
                    required = false
            )
            @RequestParam
                    String filterKey,

            @ApiParam(
                    value = "Only send out newsletter to subscribers who have this metadata value for the key specified in filterKey",
                    required = false
            )
            @RequestParam
                    String filterValue


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
