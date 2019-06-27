package io.usumu.api.metadata.controller;

import io.swagger.annotations.*;
import io.usumu.api.common.entity.ApiError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;

@ParametersAreNonnullByDefault
@RestController
@Api(
        tags = "Metadata"
)
public class MetadataRetrieveApi {
    @ApiResponses({
            @ApiResponse(code = 200, message = "The updated metadata is returned.", response = MetadataRetrieveResponse.class),
            @ApiResponse(code = 404, message = "The subscription was not found with the details in question.", response = ApiError.class)
    })
    @ApiOperation(
            nickname = "retrieveMetadata",
            value = "Retrieve metadata for subscription",
            notes = "Retrieve a set of key-value metadata entries for a subscription",
            consumes = "application/json",
            produces = "application/json"
    )
    @RequestMapping(value = "/subscriptions/{value}/metadata", method = RequestMethod.GET)
    public MetadataUpdateApi.MetadataUpdateResponse update(
            @ApiParam(
                    value = "Subscription ID, or subscriber contact info (EMAIL or PHONE in international format)",
                    required = true
            )
            @PathVariable
                    String value
    ) {
        return null;
    }

    public static class MetadataRetrieveResponse {
        public final String subscriptionId;
        public final Map<String, String> metadata;

        public MetadataRetrieveResponse(String subscriptionId, Map<String, String> metadata) {
            this.subscriptionId = subscriptionId;
            this.metadata = metadata;
        }
    }
}
