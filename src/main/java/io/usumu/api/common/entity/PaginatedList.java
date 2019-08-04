package io.usumu.api.common.entity;

import java.util.List;

public class PaginatedList<T> {
    public final String continuationToken;
    public final List<T> items;

    public PaginatedList(
        String continuationToken,
        List<T> items
    ) {
        this.continuationToken = continuationToken;
        this.items = items;
    }
}
