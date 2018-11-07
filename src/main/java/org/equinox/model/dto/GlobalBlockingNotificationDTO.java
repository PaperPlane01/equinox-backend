package org.equinox.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.equinox.model.domain.NotificationType;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GlobalBlockingNotificationDTO implements NotificationDTO {
    private Long id;
    private GlobalBlockingDTO globalBlocking;
    private Long recipientId;
    private final NotificationType type = NotificationType.GLOBAL_BLOCKING;
    private boolean read;
}
