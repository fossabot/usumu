package io.usumu.api.common.resource;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class LinkedResource<LINK_TYPE> extends Resource {
    @JsonProperty("_links")
    public final LINK_TYPE links;

    public LinkedResource(String type, LINK_TYPE links) {
        super(type);
        this.links = links;
    }
}
