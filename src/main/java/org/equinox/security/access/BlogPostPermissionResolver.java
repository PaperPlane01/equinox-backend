package org.equinox.security.access;

import lombok.RequiredArgsConstructor;
import org.equinox.model.domain.BlogRole;
import org.equinox.model.dto.BlogPostDTO;
import org.equinox.model.dto.CurrentUserDTO;
import org.equinox.service.BlogPostService;
import org.equinox.service.UserService;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class BlogPostPermissionResolver {
    private final BlogPostService blogPostService;
    private final UserService userService;

    public boolean canSaveBlogPost(Long blogId) {
        CurrentUserDTO currentUser = userService.getCurrentUser();
        boolean result = !currentUser.isBlockedGlobally()
                && (currentUser.getOwnedBlogs().stream().anyMatch(id -> Objects.equals(id, blogId))
                || currentUser.getManagedBlogs().stream()
                .anyMatch(managedBlog -> Objects.equals(managedBlog.getBlogId(), blogId)
                        && managedBlog.getBlogRole().equals(BlogRole.EDITOR)));
        System.out.println("Can save block post: " + result);
        return result;
    }

    public boolean canUpdateBlogPost(Long blogPostId) {
        BlogPostDTO blogPost = blogPostService.findById(blogPostId);
        CurrentUserDTO currentUser = userService.getCurrentUser();

        return !currentUser.isBlockedGlobally()
                && (currentUser.getOwnedBlogs().stream()
                .anyMatch(blogId -> Objects.equals(blogId, blogPost.getBlogId()))
                || currentUser.getManagedBlogs().stream()
                .anyMatch(managedBlog -> Objects.equals(managedBlog.getBlogId(), blogPost.getBlogId())
                        && managedBlog.getBlogRole().equals(BlogRole.MODERATOR))
                && Objects.equals(blogPost.getPublisher().getId(), currentUser.getId()));

    }

    public boolean canDeleteBlogPost(Long blogPostId) {
        CurrentUserDTO currentUser = userService.getCurrentUser();
        BlogPostDTO blogPost = blogPostService.findById(blogPostId);

        return currentUser.getAuthorities().stream()
                .anyMatch(authority -> authority.getName().equalsIgnoreCase("role_admin"))
                || currentUser.getOwnedBlogs().stream()
                .anyMatch(blogId -> Objects.equals(blogPost.getBlogId(), blogId))
                || currentUser.getManagedBlogs().stream()
                .anyMatch(managedBlog -> Objects.equals(managedBlog.getBlogId(), blogPost.getBlogId())
                        && managedBlog.getBlogRole().equals(BlogRole.MODERATOR))
                && Objects.equals(blogPost.getPublisher().getId(), currentUser.getId());
    }

    public boolean canViewDeletedBlogPost(Long blogPostId) {
        CurrentUserDTO currentUser = userService.getCurrentUser();
        BlogPostDTO blogPost = blogPostService.findDeletedById(blogPostId);

        return currentUser.getId().equals(blogPost.getDeletedByUserId())
                && (currentUser.getOwnedBlogs().stream()
                .anyMatch(blogId -> blogPost.getBlogId().equals(blogId))
                || currentUser.getManagedBlogs().stream()
                .anyMatch(managedBlog -> managedBlog.getBlogId().equals(blogPost.getBlogId())
                        && managedBlog.getBlogRole().equals(BlogRole.MODERATOR))
                || currentUser.getAuthorities().stream()
                .anyMatch(authority -> authority.getName().equalsIgnoreCase("ROLE_ADMIN")));
    }

    public boolean canRestoreBlogPost(Long blogPostId) {
        CurrentUserDTO currentUser = userService.getCurrentUser();
        BlogPostDTO blogPost = blogPostService.findDeletedById(blogPostId);
        return blogPost.getDeletedByUserId().equals(currentUser.getId())
                && (currentUser.getOwnedBlogs().stream()
                .anyMatch(blogId -> blogPost.getBlogId().equals(blogId))
                || currentUser.getManagedBlogs().stream()
                .anyMatch(managedBlog -> managedBlog.getBlogId().equals(blogPost.getBlogId()) && managedBlog.getBlogRole().equals(BlogRole.MODERATOR))
                || currentUser.getAuthorities().stream()
                .anyMatch(authority -> authority.getName().equalsIgnoreCase("ROLE_ADMIN")));
    }
}
