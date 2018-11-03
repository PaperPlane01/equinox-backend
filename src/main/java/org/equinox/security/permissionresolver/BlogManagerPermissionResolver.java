package org.equinox.security.permissionresolver;

import lombok.RequiredArgsConstructor;
import org.equinox.model.dto.BlogDTO;
import org.equinox.model.dto.CurrentUserDTO;
import org.equinox.service.BlogService;
import org.equinox.service.SubscriptionService;
import org.equinox.service.UserService;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class BlogManagerPermissionResolver {
    private final UserService userService;
    private final BlogService blogService;
    private final SubscriptionService subscriptionService;

    public boolean canCreateBlogManager(Long blogId) {
        CurrentUserDTO currentUser = userService.getCurrentUser();
        return currentUser.getOwnedBlogs().stream().anyMatch(id -> Objects.equals(id, blogId));
    }

    public boolean canUpdateBlogManager(Long blogId) {
        CurrentUserDTO currentUser = userService.getCurrentUser();
        return currentUser.getOwnedBlogs().stream().anyMatch(id -> Objects.equals(id, blogId));
    }

    public boolean canDeleteBlogManager(Long blogId) {
        CurrentUserDTO currentUser = userService.getCurrentUser();
        return currentUser.getOwnedBlogs().stream().anyMatch(id -> Objects.equals(id, blogId));
    }

    public boolean canSeeBlogManagers(Long blogId) {
        CurrentUserDTO currentUser = userService.getCurrentUser();
        BlogDTO blog = blogService.findById(blogId);

        switch (blog.getBlogManagersVisibilityLevel()) {
            case PUBLIC:
                return true;
            case REGISTERED_USERS:
                return currentUser != null;
            case SUBSCRIBERS:
                return subscriptionService.isUserSubscribedToBlog(currentUser.getId(), blogId);
            case MANAGERS:
                return currentUser.getManagedBlogs().stream().anyMatch(managedBlog
                        -> Objects.equals(managedBlog.getBlogId(), blogId));
            case OWNER:
                return currentUser.getOwnedBlogs().stream().anyMatch(id -> Objects.equals(id, blogId));
            default:
                return false;
        }
    }
}
