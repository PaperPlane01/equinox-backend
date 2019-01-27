package aphelion.mapper;

import aphelion.exception.BlogBlockingNotFoundException;
import aphelion.exception.BlogPostNotFoundException;
import aphelion.exception.CommentLikeNotFoundException;
import aphelion.exception.CommentNotFoundException;
import aphelion.exception.EntityNotFoundException;
import aphelion.exception.GlobalBlockingNotFoundException;
import aphelion.model.domain.BlogBlocking;
import aphelion.model.domain.BlogPost;
import aphelion.model.domain.Comment;
import aphelion.model.domain.CommentLike;
import aphelion.model.domain.GlobalBlocking;
import aphelion.model.domain.Notification;
import aphelion.model.dto.BlogBlockingNotificationDTO;
import aphelion.model.dto.GlobalBlockingNotificationDTO;
import aphelion.model.dto.NewBlogPostNotificationDTO;
import aphelion.model.dto.NewCommentLikeNotificationDTO;
import aphelion.model.dto.NewCommentReplyNotificationDTO;
import aphelion.model.dto.NotificationDTO;
import aphelion.repository.BlogBlockingRepository;
import aphelion.repository.BlogPostRepository;
import aphelion.repository.CommentLikeRepository;
import aphelion.repository.CommentRepository;
import aphelion.repository.GlobalBlockingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationToNotificationDTOMapperImpl implements NotificationToNotificationDTOMapper {
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final BlogPostRepository blogPostRepository;
    private final BlogBlockingRepository blogBlockingRepository;
    private final GlobalBlockingRepository globalBlockingRepository;
    private final CommentToCommentMinifiedDTOMapper commentToCommentMinifiedDTOMapper;
    private final CommentLikeToCommentLikeDTOMapper commentLikeToCommentLikeDTOMapper;
    private final BlogPostToBlogPostMinifiedDTOMapper blogPostToBlogPostMinifiedDTOMapper;
    private final BlogBlockingToBlogBlockingDTOMapper blogBlockingToBlogBlockingDTOMapper;
    private final GlobalBlockingToGlobalBlockingDTOMapper globalBlockingToGlobalBlockingDTOMapper;

    @Override
    public NotificationDTO map(Notification notification) {
        try {
            switch (notification.getNotificationType()) {
                case NEW_COMMENT_LIKE:
                    CommentLike commentLike = commentLikeRepository.findById(notification.getNotificationGeneratorId())
                            .orElseThrow(CommentLikeNotFoundException::new);
                    return convertToNewCommentLikeNotificationDTO(notification, commentLike);
                case NEW_BLOG_POST:
                    BlogPost blogPost = blogPostRepository.findById(notification.getNotificationGeneratorId())
                            .orElseThrow(BlogPostNotFoundException::new);
                    return convertToNewBlogPostNotificationDTO(notification, blogPost);
                case NEW_COMMENT_REPLY:
                    Comment comment = commentRepository.findById(notification.getNotificationGeneratorId())
                            .orElseThrow(CommentNotFoundException::new);
                    return convertToNewCommentReplyNotificationDTO(notification, comment);
                case GLOBAL_BLOCKING:
                    GlobalBlocking globalBlocking = globalBlockingRepository.findById(notification.getNotificationGeneratorId())
                            .orElseThrow(GlobalBlockingNotFoundException::new);
                    return convertToGlobalBlockingNotificationDTO(notification, globalBlocking);
                case BLOG_BLOCKING:
                    BlogBlocking blogBlocking = blogBlockingRepository.findById(notification.getNotificationGeneratorId())
                            .orElseThrow(BlogBlockingNotFoundException::new);
                    return convertToBlogBlockingNotificationDTO(notification, blogBlocking);
                default:
                    throw new RuntimeException("Unknown notification type.");
            }
        } catch (EntityNotFoundException e) {
            return null;
        }
    }

    @Override
    public NotificationDTO map(Notification notification, Object payload) {
        switch (notification.getNotificationType()) {
            case NEW_COMMENT_LIKE:
                return convertToNewCommentLikeNotificationDTO(notification, (CommentLike) payload);
            case NEW_BLOG_POST:
                return convertToNewBlogPostNotificationDTO(notification, (BlogPost) payload);
            case NEW_COMMENT_REPLY:
                return convertToNewCommentReplyNotificationDTO(notification, (Comment) payload);
            case GLOBAL_BLOCKING:
                return convertToGlobalBlockingNotificationDTO(notification, (GlobalBlocking) payload);
            case BLOG_BLOCKING:
                return convertToBlogBlockingNotificationDTO(notification, (BlogBlocking) payload);
            default:
                throw new RuntimeException("Unknown notification type.");
        }
    }

    private NewCommentLikeNotificationDTO convertToNewCommentLikeNotificationDTO(Notification notification, CommentLike commentLike) {
        NewCommentLikeNotificationDTO newCommentLikeNotificationDTO = new NewCommentLikeNotificationDTO();

        newCommentLikeNotificationDTO.setId(notification.getId());
        newCommentLikeNotificationDTO.setRecipientId(notification.getRecipient().getId());
        newCommentLikeNotificationDTO.setCommentLike(commentLikeToCommentLikeDTOMapper.map(commentLike));
        newCommentLikeNotificationDTO.setComment(commentToCommentMinifiedDTOMapper.map(commentLike.getComment()));
        newCommentLikeNotificationDTO.setRead(notification.isRead());

        return newCommentLikeNotificationDTO;
    }

    private NewBlogPostNotificationDTO convertToNewBlogPostNotificationDTO(Notification notification, BlogPost blogPost) {
        NewBlogPostNotificationDTO newBlogPostNotificationDTO = new NewBlogPostNotificationDTO();

        newBlogPostNotificationDTO.setId(notification.getId());
        newBlogPostNotificationDTO.setRecipientId(notification.getRecipient().getId());
        newBlogPostNotificationDTO.setBlogPost(blogPostToBlogPostMinifiedDTOMapper.map(blogPost));
        newBlogPostNotificationDTO.setRead(notification.isRead());

        return newBlogPostNotificationDTO;
    }

    private NewCommentReplyNotificationDTO convertToNewCommentReplyNotificationDTO(Notification notification, Comment comment) {
        NewCommentReplyNotificationDTO newCommentReplyNotificationDTO = new NewCommentReplyNotificationDTO();

        newCommentReplyNotificationDTO.setId(notification.getId());
        newCommentReplyNotificationDTO.setRecipientId(notification.getRecipient().getId());
        newCommentReplyNotificationDTO.setReferredComment(commentToCommentMinifiedDTOMapper.map(comment.getReferredComment()));
        newCommentReplyNotificationDTO.setReply(commentToCommentMinifiedDTOMapper.map(comment));
        newCommentReplyNotificationDTO.setRead(notification.isRead());

        return newCommentReplyNotificationDTO;
    }

    private GlobalBlockingNotificationDTO convertToGlobalBlockingNotificationDTO(Notification notification, GlobalBlocking globalBlocking) {
        GlobalBlockingNotificationDTO globalBlockingNotificationDTO = new GlobalBlockingNotificationDTO();

        globalBlockingNotificationDTO.setId(notification.getId());
        globalBlockingNotificationDTO.setRecipientId(notification.getRecipient().getId());
        globalBlockingNotificationDTO.setRead(notification.isRead());
        globalBlockingNotificationDTO.setGlobalBlocking(globalBlockingToGlobalBlockingDTOMapper.map(globalBlocking));

        return globalBlockingNotificationDTO;
    }

    private BlogBlockingNotificationDTO convertToBlogBlockingNotificationDTO(Notification notification, BlogBlocking blogBlocking) {
        BlogBlockingNotificationDTO blogBlockingNotificationDTO = new BlogBlockingNotificationDTO();

        blogBlockingNotificationDTO.setId(notification.getId());
        blogBlockingNotificationDTO.setRecipientId(notification.getRecipient().getId());
        blogBlockingNotificationDTO.setRead(notification.isRead());
        blogBlockingNotificationDTO.setBlogBlocking(blogBlockingToBlogBlockingDTOMapper.map(blogBlocking));
        return blogBlockingNotificationDTO;
    }
}
