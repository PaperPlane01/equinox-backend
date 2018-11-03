package org.equinox.model.dto;

import org.equinox.model.domain.NotificationType;

public interface NotificationDTO {
    Long getId();
    Long getRecipientId();
    NotificationType getType();
    boolean isRead();
}
