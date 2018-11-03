package org.equinox.security.permissionresolver;

import org.equinox.model.dto.BlogDTO;
import org.equinox.model.dto.CurrentUserDTO;
import org.equinox.model.dto.RestoreOrDeleteBlogDTO;
import org.equinox.model.dto.UserDTO;
import org.equinox.service.BlogService;
import org.equinox.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class BlogPermissionResolver {
    private BlogService blogService;
    private UserService userService;

    @Autowired
    public BlogPermissionResolver(BlogService blogService, UserService userService) {
        this.blogService = blogService;
        this.userService = userService;
    }

    public boolean canUpdateBlog(Long id) {
        CurrentUserDTO currentUser = userService.getCurrentUser();
        BlogDTO blogDTO = blogService.findById(id);

        return blogDTO.getOwner().getId().equals(currentUser.getId());
    }

    public boolean canDeleteBlog(Long id) {
        CurrentUserDTO currentUser = userService.getCurrentUser();
        BlogDTO blog = blogService.findById(id);

        return currentUser.getAuthorities().stream().anyMatch(authority -> authority.getName().equals("ROLE_ADMIN"))
                || blog.getOwner().getId().equals(currentUser.getId());
    }

    public boolean canCreateBlog() {
        return !userService.getCurrentUser().isBlockedGlobally();
    }

    public boolean canRestoreOrDeleteBlog(Long id, RestoreOrDeleteBlogDTO restoreOrDeleteBlogDTO) {
        if (restoreOrDeleteBlogDTO.getDeleted()) {
            CurrentUserDTO currentUser = userService.getCurrentUser();
            UserDTO userWhoDeletedBlog = blogService.getUserWhoDeletedBlog(id);
            return Objects.equals(currentUser.getId(), userWhoDeletedBlog.getId());
        } else {
            return canDeleteBlog(id);
        }
    }
}
