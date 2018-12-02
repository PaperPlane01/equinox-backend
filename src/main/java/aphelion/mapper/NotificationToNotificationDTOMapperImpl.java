package aphelion.mapper;

import lombok.RequiredArgsConstructor;
import aphelion.exception.BlogBlockingNotFoundException;
import aphelion.exception.CommentLikeNotFoundException;
import aphelion.exception.BlogPostNotFoundException;
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
                    return convertToNewCommentLikeNotificationDTO(notification);
                case NEW_BLOG_POST:
                    return convertToNewBlogPostNotificationDTO(notification);
                case NEW_COMMENT_REPLY:
                    return convertToNewCommentReplyNotificationDTO(notification);
                case GLOBAL_BLOCKING:
                    return convertToGlobalBlockingNotificationDTO(notification);
                case BLOG_BLOCKING:
                    return convertToBlogBlockingNotificationDTO(notification);
                default:
                    throw new RuntimeException("Unknown notification type.");
            }
        } catch (EntityNotFoundException e) {
            return null;
        }
    }

    private NewCommentLikeNotificationDTO convertToNewCommentLikeNotificationDTO(Notification notification) {
        NewCommentLikeNotificationDTO newCommentLikeNotificationDTO = new NewCommentLikeNotificationDTO();
        CommentLike commentLike = commentLikeRepository.findById(notification.getNotificationGeneratorId())
                .orElseThrow(CommentLikeNotFoundException::new);

        newCommentLikeNotificationDTO.setId(notification.getId());
        newCommentLikeNotificationDTO.setRecipientId(notification.getRecipient().getId());
        newCommentLikeNotificationDTO.setCommentLike(commentLikeToCommentLikeDTOMapper.map(commentLike));
        newCommentLikeNotificationDTO.setComment(commentToCommentMinifiedDTOMapper.map(commentLike.getComment()));
        newCommentLikeNotificationDTO.setRead(notification.isRead());

        return newCommentLikeNotificationDTO;
    }

    private NewBlogPostNotificationDTO convertToNewBlogPostNotificationDTO(Notification notification) {
        NewBlogPostNotificationDTO newBlogPostNotificationDTO = new NewBlogPostNotificationDTO();
        BlogPost blogPost = blogPostRepository.findById(notification.getNotificationGeneratorId())
                .orElseThrow(BlogPostNotFoundException::new);

        newBlogPostNotificationDTO.setId(notification.getId());
        newBlogPostNotificationDTO.setRecipientId(notification.getRecipient().getId());
        newBlogPostNotificationDTO.setBlogPost(blogPostToBlogPostMinifiedDTOMapper.map(blogPost));
        newBlogPostNotificationDTO.setRead(notification.isRead());

        return newBlogPostNotificationDTO;
    }

    private NewCommentReplyNotificationDTO convertToNewCommentReplyNotificationDTO(Notification notification) {
        NewCommentReplyNotificationDTO newCommentReplyNotificationDTO = new NewCommentReplyNotificationDTO();
        Comment comment = commentRepository.findById(notification.getNotificationGeneratorId())
                .orElseThrow(CommentNotFoundException::new);

        newCommentReplyNotificationDTO.setId(notification.getId());
        newCommentReplyNotificationDTO.setRecipientId(notification.getRecipient().getId());
        newCommentReplyNotificationDTO.setReferredComment(commentToCommentMinifiedDTOMapper.map(comment.getReferredComment()));
        newCommentReplyNotificationDTO.setReply(commentToCommentMinifiedDTOMapper.map(comment));
        newCommentReplyNotificationDTO.setRead(notification.isRead());

        return newCommentReplyNotificationDTO;
    }

    private GlobalBlockingNotificationDTO convertToGlobalBlockingNotificationDTO(Notification notification) {
        GlobalBlockingNotificationDTO globalBlockingNotificationDTO = new GlobalBlockingNotificationDTO();
        GlobalBlocking globalBlocking = globalBlockingRepository.findById(notification.getNotificationGeneratorId())
                .orElseThrow(GlobalBlockingNotFoundException::new);

        globalBlockingNotificationDTO.setId(notification.getId());
        globalBlockingNotificationDTO.setRecipientId(notification.getRecipient().getId());
        globalBlockingNotificationDTO.setRead(notification.isRead());
        globalBlockingNotificationDTO.setGlobalBlocking(globalBlockingToGlobalBlockingDTOMapper.map(globalBlocking));

        return globalBlockingNotificationDTO;
    }

    private BlogBlockingNotificationDTO convertToBlogBlockingNotificationDTO(Notification notification) {
        BlogBlockingNotificationDTO blogBlockingNotificationDTO = new BlogBlockingNotificationDTO();
        BlogBlocking blogBlocking = blogBlockingRepository.findById(notification.getNotificationGeneratorId())
                .orElseThrow(BlogBlockingNotFoundException::new);

        blogBlockingNotificationDTO.setId(notification.getId());
        blogBlockingNotificationDTO.setRecipientId(notification.getRecipient().getId());
        blogBlockingNotificationDTO.setRead(notification.isRead());
        blogBlockingNotificationDTO.setBlogBlocking(blogBlockingToBlogBlockingDTOMapper.map(blogBlocking));
        return blogBlockingNotificationDTO;
    }
}
