package org.equinox.security.permissionresolver;

import org.equinox.model.dto.BlogPostDTO;
import org.equinox.model.dto.BlogPostLikeDTO;
import org.equinox.model.dto.CurrentUserDTO;
import org.equinox.service.BlogPostLikeService;
import org.equinox.service.BlogPostService;
import org.equinox.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BlogPostLikePermissionResolver {
    @Autowired
    private BlogPostLikeService blogPostLikeService;

    @Autowired
    private BlogPostService blogPostService;

    @Autowired
    private UserService userService;

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
