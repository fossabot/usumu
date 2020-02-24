package io.usumu.api.log.service;

import io.usumu.api.common.service.DateTimeProvider;
import io.usumu.api.log.entity.LogEntry;
import io.usumu.api.subscription.entity.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class SubscriptionLogger {
    private final LogEntryCreateService logEntryCreateService;
    private final     DateTimeProvider dateTimeProvider;
    private final Logger           logger = LoggerFactory.getLogger(this.getClass());


    public SubscriptionLogger(
        final LogEntryCreateService logEntryCreateService,
        final DateTimeProvider dateTimeProvider
    ) {
        this.logEntryCreateService = logEntryCreateService;
        this.dateTimeProvider = dateTimeProvider;
    }

    public void log(
        Subscription subscription,
        LogEntry.EntryType entryType,
        String ipAddress
    ) {
        final ZonedDateTime time = dateTimeProvider.get();
        LogEntry logEntry = new LogEntry(
            subscription.id + "-" + (new Long(time.toInstant().toEpochMilli())).toString(),
            subscription.id,
            time,
            entryType,
            ipAddress
        );
        logEntryCreateService.create(subscription, logEntry);
        logger.info("Subscription event, id: " + subscription.id + ", type: " + entryType);
    }
}
