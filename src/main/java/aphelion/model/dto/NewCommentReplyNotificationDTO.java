package aphelion.model.dto;

import aphelion.model.domain.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewCommentReplyNotificationDTO implements NotificationDTO {
    private Long id;
    private Long recipientId;
    private CommentMinifiedDTO referredComment;
    private CommentMinifiedDTO reply;
    private boolean read;
    private final NotificationType type = NotificationType.NEW_COMMENT_REPLY;
}
