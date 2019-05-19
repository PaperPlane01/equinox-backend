package aphelion.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import aphelion.model.domain.Comment;
import aphelion.model.dto.CommentMinifiedDTO;
import org.mapstruct.Mapping;

@Mapper(uses = UserToUserMinifiedDTOMapper.class)
public interface CommentToCommentMinifiedDTOMapper {
    @BeanMapping(resultType = CommentMinifiedDTO.class)
    @Mapping(source = "blogPost.id", target = "blogPostId")
    @Mapping(source = "rootComment.id", target = "rootCommentId")
    @Mapping(source = "referredComment.id", target = "referredCommentId")
    CommentMinifiedDTO map(Comment comment);
}
