package aphelion.service.impl;

import aphelion.service.TimeStampProvider;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TimeStampProviderImpl implements TimeStampProvider {
    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }

    @Override
    public LocalDateTime weekAgo() {
        return now().minusWeeks(1);
    }

    @Override
    public LocalDateTime monthAgo() {
        return now().minusMonths(1);
    }

    @Override
    public LocalDateTime yearAgo() {
        return now().minusYears(1);
    }
}
