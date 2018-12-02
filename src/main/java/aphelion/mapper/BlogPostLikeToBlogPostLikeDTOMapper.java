package aphelion.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import aphelion.model.domain.BlogPostLike;
import aphelion.model.dto.BlogPostLikeDTO;

@Mapper(uses = UserToUserDTOMapper.class)
public interface BlogPostLikeToBlogPostLikeDTOMapper {
    @BeanMapping(resultType = BlogPostLikeDTO.class)
    @Mapping(source = "blogPost.id", target = "blogPostId")
    BlogPostLikeDTO map(BlogPostLike blogPostLike);
}
