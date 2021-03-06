package aphelion.exception;

import aphelion.model.domain.Blog;
import aphelion.model.domain.BlogPost;
import aphelion.model.domain.BlogPostLike;
import aphelion.model.domain.BlogPostReport;
import aphelion.model.domain.Comment;
import aphelion.model.domain.CommentLike;
import aphelion.model.domain.CommentReport;
import aphelion.model.domain.GlobalBlocking;
import aphelion.model.domain.Notification;
import aphelion.model.domain.Subscription;
import aphelion.model.domain.User;
import com.google.common.collect.ImmutableMap;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@Component
public class EntityNotFoundExceptionFactoryImpl implements EntityNotFoundExceptionFactory {
    private static final Map<Class, Class<? extends EntityNotFoundException>> exceptionsMap;
    private static final Map<Class, String> messagesMap;

    static {
        exceptionsMap = ImmutableMap.<Class, Class<? extends EntityNotFoundException>>builder()
                .put(Blog.class, BlogNotFoundException.class)
                .put(BlogPost.class, BlogPostNotFoundException.class)
                .put(Comment.class, CommentNotFoundException.class)
                .put(BlogPostReport.class, BlogPostReportNotFoundException.class)
                .put(CommentReport.class, CommentReportNotFoundException.class)
                .put(GlobalBlocking.class, GlobalBlockingNotFoundException.class)
                .put(Subscription.class, SubscriptionNotFoundException.class)
                .put(User.class, UserNotFoundException.class)
                .put(Notification.class, NotificationNotFoundException.class)
                .put(BlogPostLike.class, BlogPostLikeNotFoundException.class)
                .put(CommentLike.class, CommentLikeNotFoundException.class)
                .build();

        messagesMap = ImmutableMap.<Class, String>builder()
                .put(Blog.class, "Could not find blog with given id: %d")
                .put(BlogPost.class, "Could not find blog post with given id: %d")
                .put(BlogPostReport.class, "Could not find blog post report with given id: %d")
                .put(Comment.class, "Could not find comment with given id: %d")
                .put(CommentReport.class, "Could not find comment report with given id: %d")
                .put(GlobalBlocking.class, "Could not find global blocking with given id: %d")
                .put(Subscription.class, "Could not find subscription with given id: %d")
                .put(User.class, "Could not find user with given id: %d")
                .put(Notification.class, "Could not find notification with given id: %d")
                .build();
    }

    @Override
    public EntityNotFoundException create(Class entityClass) {
        if (exceptionsMap.containsKey(entityClass)) {
            try {
                return exceptionsMap.get(entityClass).newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        } else {
            throw new IllegalArgumentException("No exception class was provided for " +
                    entityClass.getCanonicalName());
        }
    }

    @Override
    public EntityNotFoundException create(Class entityClass, Long entityId) {
        if (exceptionsMap.containsKey(entityClass)) {
            String message = messagesMap.get(entityClass);
            message = String.format(message, entityId);

            try {
                return exceptionsMap.get(entityClass).getConstructor(String.class).newInstance(message);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException
                    | NoSuchMethodException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        } else {
            throw new IllegalArgumentException("No exception class was provided for "
                    + entityClass.getCanonicalName());
        }
    }
}
