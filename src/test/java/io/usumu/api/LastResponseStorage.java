package io.usumu.api;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Service
public class LastResponseStorage {
    @Nullable
    public HttpResponse<JsonNode> lastResponse = null;
}