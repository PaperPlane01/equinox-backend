package aphelion.aspect;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import aphelion.annotation.NotifySubscribers;
import aphelion.async.executor.AsyncExecutor;
import aphelion.model.dto.BlogBlockingDTO;
import aphelion.model.dto.BlogPostDTO;
import aphelion.model.dto.CommentDTO;
import aphelion.model.dto.GlobalBlockingDTO;
import aphelion.model.dto.UpdatedNumberOfCommentLikesDTO;
import aphelion.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
public class SubscribersNotificationAspect {
    @Autowired
    private AsyncExecutor asyncExecutor;

    @Autowired
    private NotificationService notificationService;

    @AfterReturning(value = "@annotation(notifySubscribers)", returning = "notificationCreator")
    public void notifySubscribers(Object notificationCreator, NotifySubscribers notifySubscribers) {
        asyncExecutor.execute(() -> Arrays.stream(notifySubscribers.type()).forEach(notificationType -> {
            switch (notificationType) {
                case NEW_COMMENT_LIKE:
                    notificationService.save(((UpdatedNumberOfCommentLikesDTO) notificationCreator)
                                    .getCommentLikeId(),
                            notificationType);
                    break;
                case NEW_BLOG_POST:
                    notificationService.save(((BlogPostDTO) notificationCreator).getId(),
                            notificationType);
                    break;
                case NEW_COMMENT_REPLY:
                    notificationService.save(((CommentDTO) notificationCreator).getId(),
                            notificationType);
                    break;
                case GLOBAL_BLOCKING:
                    notificationService.save(((GlobalBlockingDTO) notificationCreator).getId(),
                            notificationType);
                    break;
                case BLOG_BLOCKING:
                    notificationService.save(((BlogBlockingDTO) notificationCreator).getId(),
                            notificationType);
                default:
                    break;
            }
        }));
    }
}
