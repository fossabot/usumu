package io.usumu.api.subscription.resource;

import io.swagger.annotations.ApiModel;
import zone.refactor.spring.hateoas.contract.Link;
import zone.refactor.spring.hateoas.contract.PartialLink;
import zone.refactor.spring.hateoas.entity.Entity;

@SuppressWarnings("WeakerAccess")
@ApiModel("SubscriptionLinks")
public final class SubscriptionResourceLinks extends Entity {
    @SuppressWarnings({"WeakerAccess", "unused"})
    public final Link up;
    @SuppressWarnings({"WeakerAccess", "unused"})
    public final Link self;
    @SuppressWarnings({"WeakerAccess", "unused"})
    private final Link logs;

    SubscriptionResourceLinks(
        PartialLink up,
        PartialLink self,
        PartialLink logs
    ) {
        this.up = up.withUpRel();
        this.self = self.withSelfRel();
        this.logs = logs.withRel("logs");
    }
}
