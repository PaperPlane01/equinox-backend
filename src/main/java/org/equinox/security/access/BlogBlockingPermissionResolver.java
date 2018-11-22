package org.equinox.security.access;

import lombok.RequiredArgsConstructor;
import org.equinox.model.domain.BlogRole;
import org.equinox.model.dto.BlogBlockingDTO;
import org.equinox.model.dto.BlogDTO;
import org.equinox.model.dto.CurrentUserDTO;
import org.equinox.service.BlogBlockingService;
import org.equinox.service.BlogService;
import org.equinox.service.UserService;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class BlogBlockingPermissionResolver {
    private final BlogBlockingService blogBlockingService;
    private final BlogService blogService;
    private final UserService userService;

    public boolean canBlockUsersInBlog(Long blogId) {
        CurrentUserDTO currentUser = userService.getCurrentUser();
        BlogDTO blog = blogService.findById(blogId);

        return Objects.equals(blog.getOwner().getId(), currentUser.getId())
                || currentUser.getManagedBlogs().stream()
                .anyMatch(managedBlog -> Objects.equals(managedBlog.getBlogId(), blogId)
                && managedBlog.getBlogRole().equals(BlogRole.MODERATOR));

    }

    public boolean canUnblockUser(Long blogBlockingId) {
        BlogBlockingDTO blogBlocking = blogBlockingService.findById(blogBlockingId);
        return canBlockUsersInBlog(blogBlocking.getBlog().getId());
    }

    public boolean canUpdateBlocking(Long blogBlockingId) {
        BlogBlockingDTO blogBlockingDTO = blogBlockingService.findById(blogBlockingId);
        return canBlockUsersInBlog(blogBlockingDTO.getBlog().getId());
    }

    public boolean canViewBlogBlockingsInBlog(Long blogId) {
        CurrentUserDTO currentUser = userService.getCurrentUser();

        return currentUser.getOwnedBlogs().stream().anyMatch(id -> Objects.equals(id, blogId))
                || currentUser.getManagedBlogs().stream()
                .anyMatch(managedBlog -> Objects.equals(managedBlog.getBlogId(), blogId));
    }

    public boolean canViewBlogBlocking(Long id) {
        BlogBlockingDTO blogBlocking = blogBlockingService.findById(id);
        return canViewBlogBlockingsInBlog(blogBlocking.getBlog().getId());
    }
}
