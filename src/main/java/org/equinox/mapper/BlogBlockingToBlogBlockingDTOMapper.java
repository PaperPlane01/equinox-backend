package org.equinox.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.equinox.model.domain.BlogBlocking;
import org.equinox.model.dto.BlogBlockingDTO;

@Mapper(uses = {UserToCurrentUserDTOMapper.class, BlogToBlogMinifiedDTOMapper.class})
public interface BlogBlockingToBlogBlockingDTOMapper {
    @BeanMapping(resultType = BlogBlockingDTO.class)
    BlogBlockingDTO map(BlogBlocking blogBlocking);
}
