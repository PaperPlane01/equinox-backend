package aphelion.service;

import java.time.LocalDateTime;

public interface TimeStampProvider {
    LocalDateTime now();
    LocalDateTime weekAgo();
    LocalDateTime monthAgo();
    LocalDateTime yearAgo();
}
