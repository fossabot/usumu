package io.usumu.api.common.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
    value = "/",
    produces = "application/json"
)
@Api(
    tags = "Documentation"
)
@ExposesResourceFor(IndexResponse.class)
public class IndexController {
    private final EntityLinks entityLinks;

    @Autowired
    public IndexController(EntityLinks entityLinks) {
        this.entityLinks = entityLinks;
    }

    @RequestMapping(
        method = RequestMethod.GET
    )
    public IndexResponse index() {
        return new IndexResponse(
            entityLinks
        );
    }

}
