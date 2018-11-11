package org.equinox.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.equinox.model.domain.Comment;
import org.equinox.model.dto.CommentMinifiedDTO;
import org.mapstruct.Mapping;

@Mapper(uses = UserToUserDTOMapper.class)
public interface CommentToCommentMinifiedDTOMapper {
    @BeanMapping(resultType = CommentMinifiedDTO.class)
    @Mapping(source = "blogPost.id", target = "blogPostId")
    CommentMinifiedDTO map(Comment comment);
}
