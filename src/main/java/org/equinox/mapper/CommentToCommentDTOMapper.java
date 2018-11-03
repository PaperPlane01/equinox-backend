package org.equinox.mapper;

import org.equinox.model.domain.Comment;
import org.equinox.model.dto.CommentDTO;

import java.util.List;

public interface CommentToCommentDTOMapper {
    CommentDTO map(Comment comment);
    CommentDTO map(Comment comment, List<Comment> replies);
}
