package aphelion.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import aphelion.model.domain.Blog;
import aphelion.model.dto.BlogMinifiedDTO;

@Mapper
public interface BlogToBlogMinifiedDTOMapper {
    @BeanMapping(resultType = BlogMinifiedDTO.class)
    BlogMinifiedDTO map(Blog blog);
}
