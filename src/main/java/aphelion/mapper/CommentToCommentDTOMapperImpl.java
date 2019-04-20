package aphelion.mapper;

import lombok.RequiredArgsConstructor;
import aphelion.model.domain.Comment;
import aphelion.model.domain.CommentLike;
import aphelion.model.dto.CommentDTO;
import aphelion.repository.CommentLikeRepository;
import aphelion.security.AuthenticationFacade;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CommentToCommentDTOMapperImpl implements CommentToCommentDTOMapper {
    private final UserToUserMinifiedDTOMapper userToUserDTOMapper;
    private final CommentLikeRepository commentLikeRepository;
    private final AuthenticationFacade authenticationFacade;

    @Override
    public CommentDTO map(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();

        commentDTO.setId(comment.getId());
        if (comment.getReferredComment() != null) {
            commentDTO.setReferredCommentId(comment.getReferredComment().getId());
        }

        if (comment.getRootComment() != null) {
            commentDTO.setRootCommentId(comment.getRootComment().getId());
        }

        commentDTO.setNumberOfLikes(comment.getLikes().size());
        commentDTO.setCreatedAt(comment.getCreatedAt());
        commentDTO.setAuthor(userToUserDTOMapper.map(comment.getAuthor()));
        commentDTO.setBlogPostId(comment.getBlogPost().getId());
        commentDTO.setBlogId(comment.getBlogPost().getBlog().getId());

        if (comment.isDeleted()) {
            commentDTO.setDeleted(true);
            commentDTO.setDeletedAt(comment.getDeletedAt());
            commentDTO.setDeletedByUserId(comment.getDeletedBy().getId());
        } else {
            commentDTO.setDeleted(false);
            commentDTO.setContent(comment.getContent());
        }

        if (authenticationFacade.isUserAuthenticated()) {
            Optional<CommentLike> commentLike = commentLikeRepository
                    .findByUserAndComment(authenticationFacade.getCurrentUser(), comment);
            if (commentLike.isPresent()) {
                commentDTO.setLikedByCurrentUser(true);
                commentDTO.setLikeId(commentLike.get().getId());
            } else {
                commentDTO.setLikedByCurrentUser(false);
            }
        } else {
            commentDTO.setLikedByCurrentUser(false);
        }

        return commentDTO;
    }

    @Override
    public CommentDTO map(Comment comment, List<Comment> replies) {
        CommentDTO commentDTO = map(comment);
        commentDTO.setReplies(replies.stream().map(this::map).collect(Collectors.toList()));
        return commentDTO;
    }
}
