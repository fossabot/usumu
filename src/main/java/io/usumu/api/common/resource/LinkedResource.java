package io.usumu.api.common.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @param <LINK_TYPE> the object embedded as a list of links. The links should each be indexed with their relation in
 *                  the embedding object.
 */
public class LinkedResource<LINK_TYPE extends zone.refactor.spring.hateoas.contract.Entity>
    extends Resource
    implements zone.refactor.spring.hateoas.contract.LinkedEntity<LINK_TYPE>
{
    private final LINK_TYPE links;

    @SuppressWarnings({"WeakerAccess"})
    public LinkedResource(LINK_TYPE links) {
        this.links = links;
    }

    @Override
    @ApiModelProperty(name = "_links", required = true, position = 999, notes = "An object containing links to related resources.")
    @JsonProperty(value = "_links", required = true, index = 999)
    public LINK_TYPE getLinks() {
        return links;
    }
}
