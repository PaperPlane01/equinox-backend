package org.equinox.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.equinox.model.domain.Blog;
import org.equinox.model.dto.BlogMinifiedDTO;

@Mapper
public interface BlogToBlogMinifiedDTOMapper {
    @BeanMapping(resultType = BlogMinifiedDTO.class)
    BlogMinifiedDTO map(Blog blog);
}
