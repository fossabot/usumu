package io.usumu.api.subscription.controller;

import io.swagger.annotations.*;
import io.usumu.api.common.entity.PaginatedList;
import io.usumu.api.crypto.HashGenerator;
import io.usumu.api.subscription.service.SubscriptionListService;
import io.usumu.api.subscription.entity.Subscription;
import io.usumu.api.subscription.resource.SubscriptionResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zone.refactor.spring.hateoas.annotation.ListingEndpoint;
import zone.refactor.spring.hateoas.contract.LinkProvider;

@RestController
@Api(
        tags = "Subscriptions"
)
@RequestMapping("/subscriptions")
public class SubscriptionListApi {
    private final HashGenerator hashGenerator;
    private final LinkProvider linkProvider;
    private final SubscriptionListService subscriptionListService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public SubscriptionListApi(
            HashGenerator hashGenerator, LinkProvider linkProvider, SubscriptionListService subscriptionListService
    ) {
        this.hashGenerator = hashGenerator;
        this.linkProvider = linkProvider;
        this.subscriptionListService = subscriptionListService;
    }

    @ApiOperation(
        nickname = "listSubscriptions",
        value = "List subscriptions",
        notes = "List all subscriptions in the system, either confirmed or unconfirmed. The response is paginated.",
        consumes = "application/json",
        produces = "application/json"
    )
    @ApiResponses(
        value = {
            @ApiResponse(code = 200, message = "A list of current subscriptions is returned.", response = SubscriptionListResponse.class),
        }
    )
    @RequestMapping(
        method = RequestMethod.GET
    )
    @ListingEndpoint(SubscriptionResource.class)
    public SubscriptionListResponse list(
        @ApiParam(
            value = "The number of items to return",
            allowableValues = "range(1,100)"
        )
        @RequestParam(required = false)
        @Nullable
        Integer itemCount,

        @ApiParam(
            value = "Token to continue a listing",
            allowEmptyValue = true,
            allowableValues = "range(0,255)"
        )
        @RequestParam(required = false, defaultValue = "")
        @Nullable
        String continuationToken
    ) {
        PaginatedList<Subscription> subscriptions = subscriptionListService.list(itemCount, continuationToken);

        return new SubscriptionListResponse(
            subscriptions.items,
            subscriptions.continuationToken,
            hashGenerator,
            linkProvider
        );
    }
}
