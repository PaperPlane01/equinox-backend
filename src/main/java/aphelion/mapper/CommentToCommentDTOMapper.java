package aphelion.mapper;

import aphelion.model.domain.Comment;
import aphelion.model.dto.CommentDTO;

import java.util.List;

public interface CommentToCommentDTOMapper {
    CommentDTO map(Comment comment);
    CommentDTO map(Comment comment, List<Comment> replies);
}
