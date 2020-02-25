package io.usumu.api.log.controller;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.usumu.api.common.resource.SelfUpLink;
import org.springframework.lang.Nullable;
import zone.refactor.spring.hateoas.contract.Link;
import zone.refactor.spring.hateoas.contract.PartialLink;

@SuppressWarnings("WeakerAccess")
@ApiModel("LogEntryListLinks")
public class LogEntryListResponseLinks extends SelfUpLink {
    @Nullable
    @ApiModelProperty(value = "Link to the next page of results.")
    public final Link next;

    public LogEntryListResponseLinks(
        PartialLink self,
        PartialLink up,
        @Nullable
        Link next
    ) {
        super(
            self,
            up
        );
        this.next = next;
    }

    @JsonCreator
    @JsonIgnoreProperties({
        "@type"
    })
    public LogEntryListResponseLinks(
        @Nullable
        @JsonProperty(value = "next", required = false)
            zone.refactor.spring.hateoas.entity.Link next,
        @JsonProperty(value = "self", required = true)
            zone.refactor.spring.hateoas.entity.Link self,
        @JsonProperty(value = "up", required = true)
            zone.refactor.spring.hateoas.entity.Link up
    ) {
        super(self, up);
        this.next = next;
    }
}
