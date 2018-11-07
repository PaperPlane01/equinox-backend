package org.equinox.service.impl;

import lombok.RequiredArgsConstructor;
import org.equinox.exception.BlogBlockingNotFoundException;
import org.equinox.exception.CommentLikeNotFoundException;
import org.equinox.exception.GlobalBlockingNotFoundException;
import org.equinox.exception.NotificationNotFoundException;
import org.equinox.annotation.CollectionArgument;
import org.equinox.annotation.Page;
import org.equinox.annotation.PageSize;
import org.equinox.annotation.SortBy;
import org.equinox.annotation.SortingDirection;
import org.equinox.annotation.ValidateCollectionSize;
import org.equinox.annotation.ValidatePaginationParameters;
import org.equinox.exception.BlogPostNotFoundException;
import org.equinox.exception.CommentNotFoundException;
import org.equinox.exception.EntityNotFoundException;
import org.equinox.mapper.NotificationToNotificationDTOMapper;
import org.equinox.model.domain.BlogBlocking;
import org.equinox.model.domain.BlogPost;
import org.equinox.model.domain.Comment;
import org.equinox.model.domain.CommentLike;
import org.equinox.model.domain.GlobalBlocking;
import org.equinox.model.domain.Notification;
import org.equinox.model.domain.NotificationType;
import org.equinox.model.domain.Subscription;
import org.equinox.model.domain.User;
import org.equinox.model.dto.NotificationDTO;
import org.equinox.model.dto.UpdateNotificationDTO;
import org.equinox.repository.BlogBlockingRepository;
import org.equinox.repository.BlogPostRepository;
import org.equinox.repository.CommentLikeRepository;
import org.equinox.repository.CommentRepository;
import org.equinox.repository.GlobalBlockingRepository;
import org.equinox.repository.NotificationRepository;
import org.equinox.security.AuthenticationFacade;
import org.equinox.service.NotificationService;
import org.equinox.util.SortingDirectionUtils;
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
    private final BlogPostRepository blogPostRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final BlogBlockingRepository blogBlockingRepository;
    private final GlobalBlockingRepository globalBlockingRepository;
    private final NotificationToNotificationDTOMapper notificationToNotificationDTOMapper;
    private final AuthenticationFacade authenticationFacade;

    @Override
    public Notification save(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Override
    public void save(Long notificationGeneratorId, NotificationType notificationType) {
        try {
            switch (notificationType) {
                case NEW_COMMENT_REPLY:
                    createNewCommentReplyNotification(findCommentById(notificationGeneratorId));
                    break;
                case NEW_BLOG_POST:
                    createNewBlogPostNotification(findBlogPostById(notificationGeneratorId));
                    break;
                case NEW_COMMENT_LIKE:
                    createNewCommentLikeNotification(findCommentLikeById(notificationGeneratorId));
                    break;
                case BLOG_BLOCKING:
                    createBlogBlockingNotification(findBlogBlockingById(notificationGeneratorId));
                    break;
                case GLOBAL_BLOCKING:
                    createGlobalBlockingNotification(findGlobalBlockingById(notificationGeneratorId));
                    break;
            }
        } catch (EntityNotFoundException e) {
            //ignore
        }
    }

    @Override
    public NotificationDTO update(Long id, UpdateNotificationDTO updateNotificationDTO) {
        Notification notification = findNotificationById(id);
        notification.setRead(updateNotificationDTO.isRead());
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
        notification = notificationRepository.save(notification);
        return notificationToNotificationDTOMapper.map(notification);
    }

    @Override
    @ValidateCollectionSize
    public List<NotificationDTO> markAsRead(@CollectionArgument(maxSize = 50)
                                                        List<Long> notificationIds) {
        List<Notification> notifications = notificationRepository.findAllById(notificationIds);
        notifications.forEach(notification -> notification.setRead(true));
        notifications = notificationRepository.saveAll(notifications);
        return notifications.stream()
                .map(notificationToNotificationDTOMapper::map)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public NotificationDTO markAsUnread(Long id) {
        Notification notification = findNotificationById(id);
        notification.setRead(false);
        notification = notificationRepository.save(notification);
        return notificationToNotificationDTOMapper.map(notification);
    }

    @Override
    @ValidateCollectionSize
    public List<NotificationDTO> markAsUnread(@CollectionArgument(maxSize = 50)
                                                    List<Long> notificationIds) {
        List<Notification> notifications = notificationRepository.findAllById(notificationIds);
        notifications.forEach(notification -> notification.setRead(false));
        notifications = notificationRepository.saveAll(notifications);
        return notifications.stream()
                .map(notificationToNotificationDTOMapper::map)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
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

    private void createNewBlogPostNotification(BlogPost blogPost) {
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
            save(notification);
        });
    }

    private void createNewCommentReplyNotification(Comment comment) {
        if (comment.getReferredComment() == null) {
            return;
        }

        Notification notification = new Notification();

        notification.setCreatedAt(Date.from(Instant.now()));
        notification.setRead(false);
        notification.setNotificationGeneratorId(comment.getId());
        notification.setRecipient(comment.getReferredComment().getAuthor());
        save(notification);
    }

    private void createNewCommentLikeNotification(CommentLike commentLike) {
        Notification notification = new Notification();

        notification.setCreatedAt(Date.from(Instant.now()));
        notification.setNotificationType(NotificationType.NEW_COMMENT_LIKE);
        notification.setRead(false);
        notification.setNotificationGeneratorId(commentLike.getId());
        notification.setRecipient(commentLike.getComment().getAuthor());

        save(notification);
    }

    private void createBlogBlockingNotification(BlogBlocking blogBlocking) {
        Notification notification = new Notification();

        notification.setCreatedAt(Date.from(Instant.now()));
        notification.setNotificationType(NotificationType.BLOG_BLOCKING);
        notification.setRead(false);
        notification.setNotificationGeneratorId(blogBlocking.getId());
        notification.setRecipient(blogBlocking.getBlockedUser());

        save(notification);
    }

    private void createGlobalBlockingNotification(GlobalBlocking globalBlocking) {
        Notification notification = new Notification();

        notification.setCreatedAt(Date.from(Instant.now()));
        notification.setNotificationType(NotificationType.GLOBAL_BLOCKING);
        notification.setRead(false);
        notification.setNotificationGeneratorId(globalBlocking.getId());
        notification.setRecipient(globalBlocking.getBlockedUser());

        save(notification);
    }

    private BlogPost findBlogPostById(Long id) {
        return blogPostRepository.findById(id).filter(blogPost -> !blogPost.isDeleted())
                .orElseThrow(BlogPostNotFoundException::new);
    }

    private Comment findCommentById(Long id) {
        return commentRepository.findById(id).filter(comment -> !comment.isDeleted())
                .orElseThrow(CommentNotFoundException::new);
    }

    private CommentLike findCommentLikeById(Long id) {
        return commentLikeRepository.findById(id)
                .orElseThrow(CommentLikeNotFoundException::new);
    }

    private GlobalBlocking findGlobalBlockingById(Long id) {
        return globalBlockingRepository.findById(id)
                .orElseThrow(GlobalBlockingNotFoundException::new);
    }

    private BlogBlocking findBlogBlockingById(Long id) {
        return blogBlockingRepository.findById(id)
                .orElseThrow(BlogBlockingNotFoundException::new);
    }
}