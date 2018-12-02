package aphelion.security.access;

import aphelion.model.domain.BlogRole;
import aphelion.model.dto.CurrentUserDTO;
import aphelion.model.dto.SubscriptionDTO;
import aphelion.service.UserService;
import lombok.RequiredArgsConstructor;
import aphelion.service.SubscriptionService;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class SubscriptionPermissionResolver {
    private final SubscriptionService subscriptionService;
    private final UserService userService;

    public boolean canSaveSubscription(Long blogId) {
        CurrentUserDTO currentUser = userService.getCurrentUser();
        return currentUser.getBlockedInBlogs().stream().noneMatch(id -> Objects.equals(id, blogId));
    }

    public boolean canDeleteSubscription(Long id) {
        SubscriptionDTO subscription = subscriptionService.findById(id);
        CurrentUserDTO currentUser = userService.getCurrentUser();

        return currentUser.getOwnedBlogs().stream()
                .anyMatch(blogId -> Objects.equals(blogId, subscription.getBlog().getId()))
                || currentUser.getManagedBlogs().stream()
                .anyMatch(managedBlog ->
                        Objects.equals(managedBlog.getBlogId(), subscription.getBlog().getId())
                                && managedBlog.getBlogRole().equals(BlogRole.MODERATOR))
                || Objects.equals(subscription.getUser().getId(), currentUser.getId());
    }
}
