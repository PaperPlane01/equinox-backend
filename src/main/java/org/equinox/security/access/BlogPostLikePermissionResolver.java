package org.equinox.security.access;

import lombok.RequiredArgsConstructor;
import org.equinox.model.dto.BlogPostDTO;
import org.equinox.model.dto.BlogPostLikeDTO;
import org.equinox.model.dto.CurrentUserDTO;
import org.equinox.service.BlogPostLikeService;
import org.equinox.service.BlogPostService;
import org.equinox.service.UserService;
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
