package io.usumu.api.log.storage;

import io.usumu.api.log.entity.LogEntry;

import java.util.List;

public interface LogStorageList {
    List<LogEntry> list(String subscriptionId);
}
