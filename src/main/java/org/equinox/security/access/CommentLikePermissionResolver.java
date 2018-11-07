package org.equinox.security.access;

import lombok.RequiredArgsConstructor;
import org.equinox.model.dto.CommentDTO;
import org.equinox.model.dto.CommentLikeDTO;
import org.equinox.model.dto.CurrentUserDTO;
import org.equinox.service.CommentLikeService;
import org.equinox.service.CommentService;
import org.equinox.service.UserService;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CommentLikePermissionResolver {
    private final CommentService commentService;
    private final CommentLikeService commentLikeService;
    private final UserService userService;

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
