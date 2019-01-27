package aphelion.aspect;

import aphelion.async.executor.AsyncExecutor;
import aphelion.model.domain.BlogBlocking;
import aphelion.model.domain.BlogPost;
import aphelion.model.domain.Comment;
import aphelion.model.domain.CommentLike;
import aphelion.model.domain.GlobalBlocking;
import aphelion.service.NotificationService;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class SubscribersNotificationAspect {
    @Autowired
    private AsyncExecutor asyncExecutor;

    @Autowired
    private NotificationService notificationService;

    @Pointcut("execution(* aphelion.repository.CommentLikeRepository+.save(*))")
    public void whenCommentLikeSaved() {}

    @Pointcut("execution(* aphelion.repository.CommentRepository+.save(*))")
    public void whenCommentSaved() {}

    @Pointcut("execution(* aphelion.repository.BlogPostRepository+.save(*))")
    public void whenBlogPostSaved() {}

    @Pointcut("execution(* aphelion.repository.GlobalBlockingRepository+.save(*))")
    public void whenGlobalBlockingSaved() {}

    @Pointcut("execution(* aphelion.repository.BlogBlockingRepository+.save(*))")
    public void whenBlogBlockingSaved() {}

    @AfterReturning("whenCommentLikeSaved() && args(commentLike)")
    public void processCommentLikeSave(CommentLike commentLike) {
        asyncExecutor.execute(() -> notificationService.createNewCommentLikeNotification(commentLike));
    }

    @AfterReturning("whenCommentSaved() && args(comment)")
    public void processCommentSave(Comment comment) {
        asyncExecutor.execute(() -> notificationService.createNewCommentReplyNotification(comment));
    }

    @AfterReturning("whenBlogPostSaved() && args(blogPost)")
    public void processBlogPostSave(BlogPost blogPost) {
        asyncExecutor.execute(() -> notificationService.createNewBlogPostNotification(blogPost));
    }

    @AfterReturning("whenGlobalBlockingSaved() && args(globalBlocking)")
    public void processGlobalBlockingSave(GlobalBlocking globalBlocking) {
        asyncExecutor.execute(() -> notificationService.createGlobalBlockingNotification(globalBlocking));
    }

    @AfterReturning("whenBlogBlockingSaved() && args(blogBlocking)")
    public void processBlogBlockingSave(BlogBlocking blogBlocking) {
        asyncExecutor.execute(() -> notificationService.createBlogBlockingNotification(blogBlocking));
    }
}
