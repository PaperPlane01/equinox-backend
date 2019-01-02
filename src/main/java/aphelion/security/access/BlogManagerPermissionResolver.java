package aphelion.security.access;

import aphelion.model.dto.BlogDTO;
import aphelion.model.dto.BlogManagerDTO;
import aphelion.model.dto.CurrentUserDTO;
import aphelion.service.BlogManagerService;
import aphelion.service.BlogService;
import aphelion.service.SubscriptionService;
import aphelion.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class BlogManagerPermissionResolver {
    private final BlogManagerService blogManagerService;
    private final UserService userService;
    private final BlogService blogService;
    private final SubscriptionService subscriptionService;

    public boolean canCreateBlogManager(Long blogId) {
        CurrentUserDTO currentUser = userService.getCurrentUser();
        return currentUser.getOwnedBlogs().stream().anyMatch(id -> Objects.equals(id, blogId));
    }

    public boolean canUpdateBlogManager(Long blogId, Long managerId) {
        CurrentUserDTO currentUser = userService.getCurrentUser();
        BlogManagerDTO blogManager = blogManagerService.findById(managerId);
        return currentUser.getOwnedBlogs().stream().anyMatch(id -> Objects.equals(id, blogId))
                && Objects.equals(blogManager.getBlog().getId(), blogId);
    }

    public boolean canDeleteBlogManager(Long blogId, Long managerId) {
        CurrentUserDTO currentUser = userService.getCurrentUser();
        BlogManagerDTO blogManager = blogManagerService.findById(managerId);
        return currentUser.getOwnedBlogs().stream().anyMatch(id -> Objects.equals(id, blogId))
                && Objects.equals(blogManager.getBlog().getId(), blogId);
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
