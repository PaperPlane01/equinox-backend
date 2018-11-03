package org.equinox.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.equinox.model.domain.Comment;
import org.equinox.model.dto.CommentMinifiedDTO;

@Mapper(uses = UserToUserDTOMapper.class)
public interface CommentToCommentMinifiedDTOMapper {
    @BeanMapping(resultType = CommentMinifiedDTO.class)
    CommentMinifiedDTO map(Comment comment);
}
