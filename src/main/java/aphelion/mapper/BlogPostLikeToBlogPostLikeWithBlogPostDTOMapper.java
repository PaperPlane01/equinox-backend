package aphelion.mapper;

import aphelion.model.domain.BlogPostLike;
import aphelion.model.dto.BlogPostLikeWithBlogPostDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = BlogPostToBlogPostDTOMapper.class)
public interface BlogPostLikeToBlogPostLikeWithBlogPostDTOMapper {
    @BeanMapping(resultType = BlogPostLikeWithBlogPostDTO.class)
    @Mapping(source = "user.id", target = "userId")
    BlogPostLikeWithBlogPostDTO map(BlogPostLike blogPostLike);
}
