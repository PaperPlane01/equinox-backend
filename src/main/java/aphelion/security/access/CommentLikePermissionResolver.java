package aphelion.security.access;

import aphelion.model.dto.CommentDTO;
import aphelion.model.dto.CommentLikeDTO;
import aphelion.model.dto.CurrentUserDTO;
import aphelion.service.UserService;
import lombok.RequiredArgsConstructor;
import aphelion.service.CommentLikeService;
import aphelion.service.CommentService;
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
