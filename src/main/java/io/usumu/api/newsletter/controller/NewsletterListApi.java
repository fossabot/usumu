package io.usumu.api.newsletter.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.usumu.api.newsletter.entity.Newsletter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(
        tags = "Newsletter"
)
public class NewsletterListApi {
    @ApiOperation(
            nickname = "listNewsletters",
            value = "List newsletters",
            notes = "List all newsletters in the system.",
            consumes = "application/json",
            produces = "application/json"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "A newsletter successfully created.", response = NewsletterListResponse.class),
            }
    )
    @RequestMapping(
            value = "/newsletters",
            method = RequestMethod.GET
    )
    public NewsletterListResponse list(
    ) {
        return null;
    }

    public static class NewsletterListResponse {
        public final List<Newsletter> newsletters;

        public NewsletterListResponse(List<Newsletter> newsletters) {
            this.newsletters = newsletters;
        }
    }
}
