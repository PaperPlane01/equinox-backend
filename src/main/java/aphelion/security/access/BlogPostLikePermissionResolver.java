package aphelion.security.access;

import aphelion.model.dto.BlogPostDTO;
import aphelion.model.dto.BlogPostLikeDTO;
import aphelion.model.dto.CurrentUserDTO;
import aphelion.service.BlogPostService;
import aphelion.service.UserService;
import lombok.RequiredArgsConstructor;
import aphelion.service.BlogPostLikeService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BlogPostLikePermissionResolver {
    private final BlogPostLikeService blogPostLikeService;
    private final BlogPostService blogPostService;
    private final UserService userService;

    public boolean canSaveBlogPostLike(Long blogPostId) {
        BlogPostDTO blogPostDTO = blogPostService.findById(blogPostId);
        return !blogPostDTO.isLikedByCurrentUser();
    }

    public boolean canDeleteBlogPostLike(Long blogPostLikeId) {
        BlogPostLikeDTO blogPostLike = blogPostLikeService.findById(blogPostLikeId);
        CurrentUserDTO currentUser = userService.getCurrentUser();
        return blogPostLike.getUser().getId().equals(currentUser.getId());
    }
}
