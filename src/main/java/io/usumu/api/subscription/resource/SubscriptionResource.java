package io.usumu.api.subscription.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.usumu.api.common.resource.LinkedResource;
import io.usumu.api.log.resource.SubscriptionLogEntryResource;
import io.usumu.api.subscription.entity.Subscription;
import io.usumu.api.subscription.entity.SubscriptionMethod;
import io.usumu.api.subscription.entity.SubscriptionStatus;
import org.springframework.lang.Nullable;
import zone.refactor.spring.hateoas.contract.LinkProvider;
import zone.refactor.spring.hateoas.contract.PartialLink;

@ApiModel(
    "Subscription"
)
public class SubscriptionResource extends LinkedResource<SubscriptionResourceLinks> {
    @SuppressWarnings("WeakerAccess")
    @ApiModelProperty(value = "id", required = true, notes = "Identifier of this subscription.")
    @JsonProperty(value = "id", required = true)
    public final String             id;
    @SuppressWarnings("WeakerAccess")

    @ApiModelProperty(value = "method", required = true, notes = "The subscription method (EMAIL or SMS)")
    @JsonProperty(value = "method", required = true)
    public final SubscriptionMethod method;
    @SuppressWarnings("WeakerAccess")

    @Nullable
    @ApiModelProperty(
        value = "value",
        required = false,
        notes = "The actual e-mail address or phone number of the subscriber. Only available if the status is SUBSCRIBED."
    )
    @JsonProperty(value = "value", required = false)
    public final String             value;

    @SuppressWarnings("WeakerAccess")
    @ApiModelProperty(value = "status", required = true, notes = "Subscription status.")
    @JsonProperty(value = "status", required = true)
    public final SubscriptionStatus status;

    public SubscriptionResource(
        Subscription subscription,
        LinkProvider linkProvider
    ) {
        this(
            subscription,
            linkProvider.getResourceListLink(SubscriptionResource.class),
            linkProvider.getResourceLink(SubscriptionResource.class, subscription.id),
            linkProvider.getResourceListLink(SubscriptionLogEntryResource.class, subscription.id)
        );
    }

    private SubscriptionResource(
        Subscription subscription,
        PartialLink up,
        PartialLink self,
        PartialLink logs
    ) {
        super(
            new SubscriptionResourceLinks(
                up,
                self,
                logs
            )
        );
        id = subscription.id;
        method = subscription.method;
        value = subscription.value;
        status = subscription.status;
    }

}
