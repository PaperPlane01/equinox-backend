package aphelion.model.dto;

import aphelion.model.domain.NotificationType;

public interface NotificationDTO {
    Long getId();
    Long getRecipientId();
    NotificationType getType();
    boolean isRead();
}
