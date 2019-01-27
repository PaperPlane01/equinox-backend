package aphelion.service;

import aphelion.model.domain.BlogBlocking;
import aphelion.model.domain.BlogPost;
import aphelion.model.domain.Comment;
import aphelion.model.domain.CommentLike;
import aphelion.model.domain.GlobalBlocking;
import aphelion.model.domain.Notification;
import aphelion.model.domain.NotificationType;
import aphelion.model.dto.NotificationDTO;
import aphelion.model.dto.UpdateNotificationDTO;

import java.util.List;

public interface NotificationService {
    void createNewBlogPostNotification(BlogPost blogPost);
    void createNewCommentLikeNotification(CommentLike commentLike);
    void createNewCommentReplyNotification(Comment comment);
    void createGlobalBlockingNotification(GlobalBlocking globalBlocking);
    void createBlogBlockingNotification(BlogBlocking blogBlocking);
    NotificationDTO update(Long id, UpdateNotificationDTO updateNotificationDTO);
    NotificationDTO findById(Long id);
    NotificationDTO markAsRead(Long id);
    List<NotificationDTO> markAsRead(List<Long> notificationIds);
    NotificationDTO markAsUnread(Long id);
    List<NotificationDTO> markAsUnread(List<Long> notificationIds);
    List<NotificationDTO> findByIds(List<Long> notificationIds);
    void delete(Long id);
    void delete(Notification notification);
    List<NotificationDTO> findNotificationsOfCurrentUser(int page, int pageSize, String sortingDirection, String sortBy);
    List<NotificationDTO> findNotReadNotificationsOfCurrentUser(int page, int pageSize, String sortingDirection, String sortBy);

}
