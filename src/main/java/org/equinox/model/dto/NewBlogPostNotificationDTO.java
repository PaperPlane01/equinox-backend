package org.equinox.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.equinox.model.domain.NotificationType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewBlogPostNotificationDTO implements NotificationDTO {
    private Long id;
    private Long recipientId;
    private BlogPostMinifiedDTO blogPost;
    private boolean read;
    private final NotificationType type = NotificationType.NEW_BLOG_POST;
}
