package io.usumu.api.log.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.usumu.api.common.controller.IndexResponse;
import io.usumu.api.crypto.HashGenerator;
import io.usumu.api.log.entity.LogEntry;
import io.usumu.api.log.resource.LogEntryResource;
import io.usumu.api.subscription.resource.SubscriptionResource;
import org.springframework.lang.Nullable;
import zone.refactor.spring.hateoas.contract.Link;
import zone.refactor.spring.hateoas.contract.LinkProvider;
import zone.refactor.spring.hateoas.entity.LinkedEntity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("WeakerAccess")
@ApiModel("LogEntryList")
public class LogEntryListResponse extends LinkedEntity<LogEntryListResponseLinks> {
    @ApiModelProperty(
        value = "The list of log entries",
        required = true
    )
    @JsonProperty(value = "logEntries", required = true)
    public final List<LogEntryResource> logEntries;

    @Nullable
    @ApiModelProperty(
        value = "This token can be used to continue a listing."
    )
    @JsonProperty(value = "continuationToken")
    public final String continuationToken;

    @Nullable
    private static Link getNextLink(LinkProvider linkProvider, @Nullable String continuationToken) {
        if (continuationToken == null || continuationToken.isEmpty()) {
            return null;
        }
        Link next = linkProvider.getResourceListLink(SubscriptionResource.class).withNextRel();
        try {
            return new zone.refactor.spring.hateoas.entity.Link(
                next.getRel(),
                next.getHref() + "?continuationToken=" + URLEncoder.encode(continuationToken, "UTF-8")
            );
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public LogEntryListResponse(
        List<LogEntry> logEntries,
        @Nullable String continuationToken,
        HashGenerator hashGenerator,
        LinkProvider linkProvider
    ) {
        super(new LogEntryListResponseLinks(
            linkProvider.getResourceListLink(SubscriptionResource.class),
            linkProvider.getResourceLink(IndexResponse.class),
            getNextLink(linkProvider, continuationToken)
        ));
        this.logEntries = logEntries.stream().map(logEntry -> new LogEntryResource(
            logEntry, linkProvider
        )).collect(Collectors.toList());
        this.continuationToken = continuationToken == null || continuationToken.isEmpty()?null:continuationToken;
    }
}
