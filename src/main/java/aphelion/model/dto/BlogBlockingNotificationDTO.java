package aphelion.model.dto;

import aphelion.model.domain.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
