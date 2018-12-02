package aphelion.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import aphelion.model.domain.CommentLike;
import aphelion.model.dto.CommentLikeDTO;

@Mapper(uses = UserToUserDTOMapper.class)
public interface CommentLikeToCommentLikeDTOMapper {
    @BeanMapping(resultType = CommentLikeDTO.class)
    @Mapping(source = "comment.id", target = "commentId")
    CommentLikeDTO map(CommentLike commentLike);
}
