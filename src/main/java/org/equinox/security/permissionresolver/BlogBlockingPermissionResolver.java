package org.equinox.security.permissionresolver;

import org.equinox.model.domain.BlogRole;
import org.equinox.model.dto.BlogBlockingDTO;
import org.equinox.model.dto.BlogDTO;
import org.equinox.model.dto.CurrentUserDTO;
import org.equinox.service.BlogBlockingService;
import org.equinox.service.BlogManagerService;
import org.equinox.service.BlogService;
import org.equinox.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class BlogBlockingPermissionResolver {
    @Autowired
    private BlogBlockingService blogBlockingService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private BlogManagerService blogManagerService;

    @Autowired
    private UserService userService;

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
        return canBlockUsersInBlog(blogBlocking.getBlogId());
    }

    public boolean canUpdateBlocking(Long blogBlockingId) {
        BlogBlockingDTO blogBlockingDTO = blogBlockingService.findById(blogBlockingId);
        return canBlockUsersInBlog(blogBlockingDTO.getBlogId());
    }
}
