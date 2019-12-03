package io.usumu.api.subscription.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.usumu.api.crypto.HashGenerator;
import io.usumu.api.log.resource.SubscriptionLogEntryResource;
import io.usumu.api.subscription.entity.Subscription;
import org.springframework.lang.Nullable;
import zone.refactor.spring.hateoas.contract.LinkProvider;
import zone.refactor.spring.hateoas.contract.PartialLink;
import zone.refactor.spring.hateoas.entity.LinkedEntity;

@ApiModel(
    "Subscription"
)
public class SubscriptionResource extends LinkedEntity<SubscriptionResourceLinks> {
    @SuppressWarnings("WeakerAccess")
    @ApiModelProperty(value = "id", required = true)
    @JsonProperty(value = "id", required = true)
    public final String id;
    @SuppressWarnings("WeakerAccess")

    @ApiModelProperty(value = "method", required = true)
    @JsonProperty(value = "method", required = true)
    public final Subscription.Method method;
    @SuppressWarnings("WeakerAccess")

    @Nullable
    @ApiModelProperty(value = "value", required = false)
    @JsonProperty(value = "value", required = false)
    public final String value;

    @SuppressWarnings("WeakerAccess")
    @Nullable
    @ApiModelProperty(value = "verificationCode", required = false)
    @JsonProperty(value = "verificationCode", required = false)
    public final String verificationCode;

    @SuppressWarnings("WeakerAccess")
    @ApiModelProperty(value = "status", required = true)
    @JsonProperty(value = "status", required = true)
    public final Subscription.Status status;

    public SubscriptionResource(
        Subscription subscription,
        HashGenerator hashGenerator,
        LinkProvider linkProvider
    ) {
        this(
            subscription,
            hashGenerator,
            linkProvider.getResourceListLink(SubscriptionResource.class),
            linkProvider.getResourceLink(SubscriptionResource.class, subscription.id),
            linkProvider.getResourceListLink(SubscriptionLogEntryResource.class, subscription.id)
        );
    }

    private SubscriptionResource(Subscription subscription, HashGenerator hashGenerator, PartialLink up, PartialLink self, PartialLink logs) {
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
        verificationCode = subscription.getVerificationCode(hashGenerator);
    }

}
