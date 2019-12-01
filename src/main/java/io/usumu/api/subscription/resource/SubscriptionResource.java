package io.usumu.api.subscription.resource;

import io.swagger.annotations.ApiModel;
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
    public final String id;
    @SuppressWarnings("WeakerAccess")
    public final Subscription.Type type;
    @SuppressWarnings("WeakerAccess")
    @Nullable
    public final String value;
    @SuppressWarnings("WeakerAccess")
    @Nullable
    public final String verificationCode;
    @SuppressWarnings("WeakerAccess")
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
        type = subscription.type;
        value = subscription.value;
        status = subscription.status;
        verificationCode = subscription.getVerificationCode(hashGenerator);
    }

}
