package aphelion.service.impl;

import aphelion.annotation.CollectionArgument;
import aphelion.annotation.Page;
import aphelion.annotation.PageSize;
import aphelion.annotation.SortBy;
import aphelion.annotation.SortingDirection;
import aphelion.annotation.ValidateCollectionSize;
import aphelion.annotation.ValidatePaginationParameters;
import aphelion.exception.NotificationNotFoundException;
import aphelion.mapper.NotificationToNotificationDTOMapper;
import aphelion.model.domain.BlogBlocking;
import aphelion.model.domain.BlogPost;
import aphelion.model.domain.Comment;
import aphelion.model.domain.CommentLike;
import aphelion.model.domain.GlobalBlocking;
import aphelion.model.domain.Notification;
import aphelion.model.domain.NotificationType;
import aphelion.model.domain.Subscription;
import aphelion.model.domain.User;
import aphelion.model.dto.NotificationDTO;
import aphelion.model.dto.UpdateNotificationDTO;
import aphelion.repository.NotificationRepository;
import aphelion.security.AuthenticationFacade;
import aphelion.service.NotificationService;
import aphelion.service.NotificationsSender;
import aphelion.util.SortingDirectionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationToNotificationDTOMapper notificationToNotificationDTOMapper;
    private final AuthenticationFacade authenticationFacade;
    private final NotificationsSender notificationsSender;

    @Override
    public NotificationDTO update(Long id, UpdateNotificationDTO updateNotificationDTO) {
        Notification notification = findNotificationById(id);
        notification.setRead(updateNotificationDTO.isRead());
        notification = saveAndSend(notification);
        return notificationToNotificationDTOMapper.map(notification);
    }

    @Override
    public NotificationDTO findById(Long id) {
        return notificationToNotificationDTOMapper.map(findNotificationById(id));
    }

    @Override
    public NotificationDTO markAsRead(Long id) {
        Notification notification = findNotificationById(id);
        notification.setRead(true);
        notification = saveAndSend(notification);
        return notificationToNotificationDTOMapper.map(notification);
    }

    @Override
    @ValidateCollectionSize
    public List<NotificationDTO> markAsRead(@CollectionArgument(maxSize = 50)
                                                        List<Long> notificationIds) {
        List<Notification> notifications = notificationRepository.findAllById(notificationIds);
        notifications.forEach(notification -> notification.setRead(true));
        notifications = notificationRepository.saveAll(notifications);
        List<NotificationDTO> notificationDTOList = notifications.stream()
                .map(notificationToNotificationDTOMapper::map)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (!notifications.isEmpty()) {
            User recipient = notifications.get(0).getRecipient();
            notificationsSender.sendNotifications(recipient.getGeneratedUsername(), notificationDTOList);
        }
        return notificationDTOList;
    }

    @Override
    public NotificationDTO markAsUnread(Long id) {
        Notification notification = findNotificationById(id);
        notification.setRead(false);
        notification = saveAndSend(notification);
        return notificationToNotificationDTOMapper.map(notification);
    }

    @Override
    @ValidateCollectionSize
    public List<NotificationDTO> markAsUnread(@CollectionArgument(maxSize = 50)
                                                    List<Long> notificationIds) {
        List<Notification> notifications = notificationRepository.findAllById(notificationIds);
        notifications.forEach(notification -> notification.setRead(false));
        notifications = notificationRepository.saveAll(notifications);
        List<NotificationDTO> notificationDTOList = notifications.stream()
                .map(notificationToNotificationDTOMapper::map)
                .collect(Collectors.toList());
        if (!notifications.isEmpty()) {
            String recipientId = notifications.get(0).getRecipient().getGeneratedUsername();
            notificationsSender.sendNotifications(recipientId, notificationDTOList);
        }
        return notificationDTOList;
    }

    @Override
    @ValidateCollectionSize
    public List<NotificationDTO> findByIds(@CollectionArgument(maxSize = 50)
                                                       List<Long> notificationIds) {
        return notificationRepository.findAllById(notificationIds)
                .stream()
                .map(notificationToNotificationDTOMapper::map)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        Notification notification = findNotificationById(id);
        notificationRepository.delete(notification);
    }

    private Notification findNotificationById(Long id) {
        return notificationRepository.findById(id).filter(notification -> !notification.isDeleted())
                .orElseThrow(() -> new NotificationNotFoundException("Notification with " +
                        "given id " + id + " could not be found."));
    }

    @Override
    public void delete(Notification notification) {
        notificationRepository.delete(notification);
    }

    @Override
    @ValidatePaginationParameters
    public List<NotificationDTO> findNotificationsOfCurrentUser(@Page int page,
                                                                @PageSize(max = 50) int pageSize,
                                                                @SortingDirection String sortingDirection,
                                                                @SortBy(allowed = "id") String sortBy) {
        User user = authenticationFacade.getCurrentUser();
        Sort.Direction direction = SortingDirectionUtils.convertFromString(sortingDirection);
        PageRequest pageRequest = PageRequest.of(page, pageSize, direction, sortBy);
        return notificationRepository.findByRecipient(user, pageRequest)
                .stream()
                .map(notificationToNotificationDTOMapper::map)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    @ValidatePaginationParameters
    public List<NotificationDTO> findNotReadNotificationsOfCurrentUser(@Page int page,
                                                                       @PageSize(max = 50) int pageSize,
                                                                       @SortingDirection String sortingDirection,
                                                                       @SortBy(allowed = "id") String sortBy) {
        User user = authenticationFacade.getCurrentUser();
        Sort.Direction direction = SortingDirectionUtils.convertFromString(sortingDirection);
        PageRequest pageRequest = PageRequest.of(page, pageSize, direction, sortBy);
        return notificationRepository.findByRecipientAndRead(user, false, pageRequest)
                .stream()
                .map(notificationToNotificationDTOMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public void createNewBlogPostNotification(BlogPost blogPost) {
        if (blogPost.getLastUpdateDate() != null) {
            //means that blog post is not new - no deed to create new notification
            return;
        }
        List<User> subscribers = blogPost.getBlog()
                .getSubscriptions()
                .stream()
                .map(Subscription::getUser)
                .collect(Collectors.toList());

        subscribers.forEach(subscriber -> {
            Notification notification = new Notification();

            notification.setCreatedAt(Date.from(Instant.now()));
            notification.setNotificationType(NotificationType.NEW_BLOG_POST);
            notification.setRead(false);
            notification.setNotificationGeneratorId(blogPost.getId());
            notification.setRecipient(subscriber);
            saveAndSend(notification, blogPost);
        });
    }

    @Override
    public void createNewCommentReplyNotification(Comment comment) {
        if (comment.getReferredComment() == null) {
            return;
        }

        if (comment.getLastUpdateDate() != null) {
            //means that comment is not new - no need to create new notification
            return;
        }

        Notification notification = new Notification();

        notification.setCreatedAt(Date.from(Instant.now()));
        notification.setRead(false);
        notification.setNotificationGeneratorId(comment.getId());
        notification.setRecipient(comment.getReferredComment().getAuthor());
        notification.setNotificationType(NotificationType.NEW_COMMENT_REPLY);
        saveAndSend(notification, comment);
    }

    @Override
    public void createNewCommentLikeNotification(CommentLike commentLike) {
        Notification notification = new Notification();

        System.out.println("in createNewCommentLikeNotification()");
        notification.setCreatedAt(Date.from(Instant.now()));
        notification.setNotificationType(NotificationType.NEW_COMMENT_LIKE);
        notification.setRead(false);
        notification.setNotificationGeneratorId(commentLike.getId());
        notification.setRecipient(commentLike.getComment().getAuthor());

        saveAndSend(notification, commentLike);
    }

    @Override
    public void createBlogBlockingNotification(BlogBlocking blogBlocking) {
        Notification notification = new Notification();

        notification.setCreatedAt(Date.from(Instant.now()));
        notification.setNotificationType(NotificationType.BLOG_BLOCKING);
        notification.setRead(false);
        notification.setNotificationGeneratorId(blogBlocking.getId());
        notification.setRecipient(blogBlocking.getBlockedUser());

        saveAndSend(notification, blogBlocking);
    }

    @Override
    public void createGlobalBlockingNotification(GlobalBlocking globalBlocking) {
        Notification notification = new Notification();

        notification.setCreatedAt(Date.from(Instant.now()));
        notification.setNotificationType(NotificationType.GLOBAL_BLOCKING);
        notification.setRead(false);
        notification.setNotificationGeneratorId(globalBlocking.getId());
        notification.setRecipient(globalBlocking.getBlockedUser());

        saveAndSend(notification, globalBlocking);
    }

    private Notification saveAndSend(Notification notification) {
        notification = notificationRepository.save(notification);
        String recipientId = notification.getRecipient().getGeneratedUsername();
        NotificationDTO notificationDTO = notificationToNotificationDTOMapper.map(notification);
        notificationsSender.sendNotifications(recipientId, notificationDTO);
        return notification;
    }

    private Notification saveAndSend(Notification notification, Object payload) {
        notification =  notificationRepository.save(notification);
        String recipientId = notification.getRecipient().getGeneratedUsername();
        NotificationDTO notificationDTO = notificationToNotificationDTOMapper.map(notification, payload);
        System.out.println("Notification to be sent: " + notificationDTO);
        notificationsSender.sendNotifications(recipientId, notificationDTO);
        return notification;
    }
}