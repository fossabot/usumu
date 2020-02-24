package io.usumu.api.common.service;

import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.function.Supplier;

@Service
public class DateTimeProvider implements Supplier<ZonedDateTime> {
    @Override
    public ZonedDateTime get() {
        return ZonedDateTime.now();
    }
}
