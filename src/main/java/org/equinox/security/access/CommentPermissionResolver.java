package org.equinox.security.access;

import lombok.RequiredArgsConstructor;
import org.equinox.model.domain.BlogRole;
import org.equinox.model.dto.BlogPostDTO;
import org.equinox.model.dto.CommentDTO;
import org.equinox.model.dto.CurrentUserDTO;
import org.equinox.model.dto.RestoreOrDeleteCommentDTO;
import org.equinox.service.BlogPostService;
import org.equinox.service.CommentService;
import org.equinox.service.UserService;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CommentPermissionResolver {
    private final CommentService commentService;
    private final BlogPostService blogPostService;
    private final UserService userService;

    public boolean canSaveComment(Long blogPostId) {
        CurrentUserDTO currentUser = userService.getCurrentUser();
        BlogPostDTO blogPost = blogPostService.findById(blogPostId);

        return !currentUser.isBlockedGlobally()
                && (currentUser.getOwnedBlogs().stream().anyMatch(blogId -> Objects.equals(blogId, blogPost.getBlogId()))
                || currentUser.getManagedBlogs().stream().anyMatch(managedBlog -> Objects.equals(managedBlog.getBlogId(), blogPost.getBlogId()))
                || currentUser.getBlockedInBlogs().stream().noneMatch(blogId -> Objects.equals(blogId, blogPost.getBlogId())));

    }

    public boolean canUpdateComment(Long commentId) {
        CurrentUserDTO currentUser = userService.getCurrentUser();
        CommentDTO comment = commentService.findById(commentId);

        return !currentUser.isBlockedGlobally()
                && (currentUser.getOwnedBlogs().stream().anyMatch(blogId -> Objects.equals(blogId, comment.getBlogId()))
                && Objects.equals(currentUser.getId(), comment.getAuthor().getId())
                || currentUser.getManagedBlogs().stream().anyMatch(managedBlog -> Objects.equals(managedBlog.getBlogId(), comment.getBlogId()))
                && Objects.equals(currentUser.getId(), comment.getAuthor().getId())
                || currentUser.getBlockedInBlogs().stream().noneMatch(blogId -> Objects.equals(blogId, comment.getBlogId()))
                && Objects.equals(currentUser.getId(), comment.getAuthor().getId()));
    }

    public boolean canDeleteComment(Long commentId) {
        CommentDTO comment = commentService.findById(commentId);
        CurrentUserDTO currentUser = userService.getCurrentUser();

        return currentUser.getAuthorities().stream().anyMatch(authority -> authority.getName().equalsIgnoreCase("role_admin"))
                || currentUser.getOwnedBlogs().stream().anyMatch(blogId -> Objects.equals(blogId, comment.getBlogId()))
                || currentUser.getManagedBlogs().stream().anyMatch(managedBlog -> Objects.equals(managedBlog.getBlogId(), comment.getBlogId()))
                || !currentUser.isBlockedGlobally()
                && currentUser.getBlockedInBlogs().stream().noneMatch(blogId -> Objects.equals(blogId, comment.getBlogId()))
                && Objects.equals(currentUser.getId(), comment.getAuthor().getId());

    }

    public boolean canRestoreOrDeleteComment(Long commentId, RestoreOrDeleteCommentDTO restoreOrDeleteCommentDTO) {
        if (restoreOrDeleteCommentDTO.getDeleted()) {
            return canDeleteComment(commentId);
        }

        CommentDTO comment = commentService.findById(commentId);
        CurrentUserDTO currentUser = userService.getCurrentUser();

        return comment.isDeleted() && Objects.equals(comment.getDeletedByUserId(), currentUser.getId());
    }
}
