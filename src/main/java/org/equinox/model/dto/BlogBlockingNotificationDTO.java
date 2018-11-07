package org.equinox.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.equinox.model.domain.NotificationType;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BlogBlockingNotificationDTO implements NotificationDTO {
    private Long id;
    private BlogBlockingDTO blogBlocking;
    private final NotificationType type = NotificationType.BLOG_BLOCKING;
    private boolean read;
    private Long recipientId;
}
