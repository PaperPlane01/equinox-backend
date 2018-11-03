package org.equinox.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.equinox.model.domain.BlogPostLike;
import org.equinox.model.dto.BlogPostLikeDTO;

@Mapper(uses = UserToUserDTOMapper.class)
public interface BlogPostLikeToBlogPostLikeDTOMapper {
    @BeanMapping(resultType = BlogPostLikeDTO.class)
    @Mapping(source = "blogPost.id", target = "blogPostId")
    BlogPostLikeDTO map(BlogPostLike blogPostLike);
}
