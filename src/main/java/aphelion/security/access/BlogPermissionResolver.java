package aphelion.security.access;

import aphelion.model.dto.BlogDTO;
import aphelion.model.dto.CurrentUserDTO;
import aphelion.model.dto.RestoreOrDeleteBlogDTO;
import aphelion.model.dto.UserDTO;
import aphelion.service.UserService;
import lombok.RequiredArgsConstructor;
import aphelion.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class BlogPermissionResolver {
    private final UserService userService;
    private BlogService blogService;

    @Autowired
    public void setBlogService(BlogService blogService) {
        this.blogService = blogService;
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
