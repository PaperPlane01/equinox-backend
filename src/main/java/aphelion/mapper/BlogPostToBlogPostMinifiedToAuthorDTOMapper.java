package aphelion.mapper;

import aphelion.model.domain.BlogPost;
import aphelion.model.dto.BlogPostMinifiedWithAuthorDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;

@Mapper
public interface BlogPostToBlogPostMinifiedToAuthorDTOMapper {
    @BeanMapping(resultType = BlogPostMinifiedWithAuthorDTO.class)
    BlogPostMinifiedWithAuthorDTO map(BlogPost blogPost);
}
