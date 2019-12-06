package io.usumu.api.common.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import zone.refactor.spring.hateoas.contract.Link;

@ApiModel
@SuppressWarnings("WeakerAccess")
public class SelfLink extends Resource implements zone.refactor.spring.hateoas.contract.SelfLink {
    private final Link self;

    public SelfLink(zone.refactor.spring.hateoas.contract.PartialLink self) {
        this.self = self.withSelfRel();
    }

    @Override
    @ApiModelProperty(name = "self", value = "Link to the current resource", required = true, position = 1)
    @JsonProperty(value = "self", required = true, index = 1)
    public zone.refactor.spring.hateoas.contract.Link getSelf() {
        return self;
    }
}
