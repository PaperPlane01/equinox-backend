package org.equinox.security.permissionresolver;

import org.equinox.model.dto.CommentDTO;
import org.equinox.model.dto.CommentLikeDTO;
import org.equinox.model.dto.CurrentUserDTO;
import org.equinox.service.CommentLikeService;
import org.equinox.service.CommentService;
import org.equinox.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CommentLikePermissionResolver {
    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentLikeService commentLikeService;

    @Autowired
    private UserService userService;

    public boolean canSaveCommentLike(Long commentId) {
        CommentDTO comment = commentService.findById(commentId);
        return !comment.isLikedByCurrentUser();
    }

    public boolean canDeleteCommentLike(Long commentLikeId) {
        CurrentUserDTO currentUser = userService.getCurrentUser();

        if (currentUser != null) {
            CommentLikeDTO commentLike = commentLikeService.findById(commentLikeId);
            return Objects.equals(currentUser.getId(), commentLike.getUser().getId());
        } else {
            return false;
        }
    }
}
