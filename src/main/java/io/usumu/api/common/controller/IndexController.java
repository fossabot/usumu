package io.usumu.api.common.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import zone.refactor.spring.hateoas.contract.LinkProvider;

@RestController
@RequestMapping(
    value = "/",
    produces = "application/json"
)
@Api(
    tags = "Documentation"
)
public class IndexController {
    private final LinkProvider linkProvider;

    @Autowired
    public IndexController(LinkProvider linkProvider) {
        this.linkProvider = linkProvider;
    }

    @RequestMapping(
        method = RequestMethod.GET
    )
    public IndexResponse index() {
        return new IndexResponse(
            linkProvider
        );
    }

}
