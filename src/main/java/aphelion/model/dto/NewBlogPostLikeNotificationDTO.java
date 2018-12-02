package aphelion.model.dto;

import aphelion.model.domain.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewBlogPostLikeNotificationDTO implements NotificationDTO {
    private Long id;
    private BlogPostLikeDTO blogPostLike;
    private BlogPostMinifiedDTO blogPost;
    private Long recipientId;
    private boolean read;
    private final NotificationType type = NotificationType.NEW_BLOG_POST_LIKE;
}
