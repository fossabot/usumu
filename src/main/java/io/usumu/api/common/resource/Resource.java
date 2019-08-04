package io.usumu.api.common.resource;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class Resource {
    @JsonProperty("@type")
    public final String type;

    protected Resource(String type) {
        this.type = type;
    }
}
