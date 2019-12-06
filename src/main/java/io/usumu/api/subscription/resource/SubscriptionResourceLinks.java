package io.usumu.api.subscription.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.usumu.api.common.resource.SelfUpLink;
import zone.refactor.spring.hateoas.contract.Link;
import zone.refactor.spring.hateoas.contract.PartialLink;

@SuppressWarnings("WeakerAccess")
@ApiModel("SubscriptionLinks")
public final class SubscriptionResourceLinks extends SelfUpLink {
    @ApiModelProperty(required = true, notes = "Link to the logs for this subscription.")
    @JsonProperty(value = "logs", required = true)
    public final Link logs;

    SubscriptionResourceLinks(
        PartialLink up,
        PartialLink self,
        PartialLink logs
    ) {
        super(up, self);
        this.logs = logs.withRel("logs");
    }
}
