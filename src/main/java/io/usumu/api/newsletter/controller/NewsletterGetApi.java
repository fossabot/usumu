package io.usumu.api.newsletter.controller;

import io.swagger.annotations.*;
import io.usumu.api.newsletter.entity.Newsletter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@RestController
@Api(
        tags = "Newsletter"
)
public class NewsletterGetApi {
    @ApiOperation(
            nickname = "getNewsletter",
            value = "Retrieve a single newsletter",
            notes = "Retrieve the status of a single newsletter",
            consumes = "application/json",
            produces = "application/json"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "A newsletter successfully created.", response = NewsletterGetResponse.class),
            }
    )
    @RequestMapping(
            value = "/newsletters/{id}",
            method = RequestMethod.GET
    )
    public NewsletterGetResponse get(
            @ApiParam(
                    value = "Newsletter ID",
                    required = true
            )
            @PathVariable
            String id
    ) {
        return null;
    }

    public static class NewsletterGetResponse {
        public final Newsletter newsletter;

        public NewsletterGetResponse(Newsletter newsletter) {
            this.newsletter = newsletter;
        }
    }
}
