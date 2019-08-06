package io.usumu.api.metadata.controller;

import io.swagger.annotations.*;
import io.usumu.api.subscription.exception.SubscriptionNotFound;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Api(
        tags = "Metadata"
)
public class MetadataUpdateApi {
    @ApiResponses({
            @ApiResponse(code = 200, message = "The metadata was successfully updated.", response = MetadataUpdateResponse.class),
            @ApiResponse(code = 404, message = "The subscription was not found with the details in question.", response = SubscriptionNotFound.class)
    })
    @ApiOperation(
            nickname = "updateMetadata",
            value = "Update metadata for subscription",
            notes = "Update the metadata for a subscription by supplying a set of key-value pairs available in templates.",
            consumes = "application/json",
            produces = "application/json"
    )
    @RequestMapping(value = "/subscriptions/{value}/metadata", method = RequestMethod.PATCH)
    public MetadataUpdateResponse update(
            @ApiParam(
                    value = "Subscription ID, or subscriber contact info (EMAIL or PHONE in international format)",
                    required = true
            )
            @PathVariable
            String value,
            @ApiParam(
                    value = "Metadata in key-value format",
                    required = true
            )
            @RequestParam
            Map<String, String> metadata
    ) {
        return null;
    }

    public static class MetadataUpdateResponse {
        @SuppressWarnings({"WeakerAccess", "unused"})
        public final String subscriptionId;
        @SuppressWarnings({"WeakerAccess", "unused"})
        public final Map<String, String> metadata;

        public MetadataUpdateResponse(String subscriptionId, Map<String, String> metadata) {
            this.subscriptionId = subscriptionId;
            this.metadata = metadata;
        }
    }
}
