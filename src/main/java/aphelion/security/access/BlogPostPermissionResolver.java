package aphelion.security.access;

import aphelion.model.domain.BlogPostPublisherType;
import aphelion.model.domain.BlogRole;
import aphelion.model.dto.BlogPostDTO;
import aphelion.model.dto.CurrentUserDTO;
import aphelion.service.BlogPostService;
import aphelion.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class BlogPostPermissionResolver {
    private BlogPostService blogPostService;
    private final UserService userService;

    @Autowired
    public void setBlogPostService(BlogPostService blogPostService) {
        this.blogPostService = blogPostService;
    }

    public boolean canSaveBlogPost(Long blogId) {
        CurrentUserDTO currentUser = userService.getCurrentUser();
        return !currentUser.isBlockedGlobally()
                && (currentUser.getOwnedBlogs().stream().anyMatch(id -> Objects.equals(id, blogId))
                || currentUser.getManagedBlogs().stream()
                .anyMatch(managedBlog -> Objects.equals(managedBlog.getBlogId(), blogId)
                        && managedBlog.getBlogRole().equals(BlogRole.EDITOR)));
    }

    public boolean canUpdateBlogPost(Long blogPostId) {
        BlogPostDTO blogPost = blogPostService.findById(blogPostId);
        return canUpdateBlogPost(blogPost);
    }

    public boolean canUpdateBlogPost(BlogPostDTO blogPost) {
        CurrentUserDTO currentUser = userService.getCurrentUser();
        return !currentUser.isBlockedGlobally()
                && (currentUser.getOwnedBlogs().stream()
                .anyMatch(blogId -> Objects.equals(blogId, blogPost.getBlogId()))
                || currentUser.getManagedBlogs().stream()
                .anyMatch(managedBlog -> Objects.equals(managedBlog.getBlogId(), blogPost.getBlogId())
                        && managedBlog.getBlogRole().equals(BlogRole.EDITOR))
                && Objects.equals(blogPost.getPublisher().getId(), currentUser.getId()));
    }

    public boolean canDeleteBlogPost(Long blogPostId) {
        BlogPostDTO blogPost = blogPostService.findById(blogPostId);
        return canDeleteBlogPost(blogPost);
    }

    public boolean canDeleteBlogPost(BlogPostDTO blogPost) {
        CurrentUserDTO currentUser = userService.getCurrentUser();

        return currentUser.getAuthorities().stream()
                .anyMatch(authority -> authority.getName().equalsIgnoreCase("role_admin"))
                || currentUser.getOwnedBlogs().stream()
                .anyMatch(blogId -> Objects.equals(blogPost.getBlogId(), blogId))
                || currentUser.getManagedBlogs().stream()
                .anyMatch(managedBlog -> Objects.equals(managedBlog.getBlogId(), blogPost.getBlogId())
                        && managedBlog.getBlogRole().equals(BlogRole.EDITOR))
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
                        && managedBlog.getBlogRole().equals(BlogRole.EDITOR))
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

    public boolean canSeeBlogPostAuthor(Long blogPostId) {
        CurrentUserDTO currentUser = userService.getCurrentUser();
        BlogPostDTO blogPost = blogPostService.findById(blogPostId);

        return blogPost.getPublishedBy().equals(BlogPostPublisherType.BLOG_POST_AUTHOR)
                || currentUser != null && (currentUser
                .getAuthorities()
                .stream()
                .anyMatch(authority -> authority.getName().toLowerCase().equals("role_admin"))
                || currentUser.getOwnedBlogs()
                .stream()
                .anyMatch(blogId -> Objects.equals(blogId, blogPost.getBlogId())));
    }
}
