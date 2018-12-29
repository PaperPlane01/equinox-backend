package aphelion.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import aphelion.model.domain.BlogBlocking;
import aphelion.model.dto.BlogBlockingDTO;

@Mapper(uses = {UserToUserDTOMapper.class, BlogToBlogMinifiedDTOMapper.class})
public interface BlogBlockingToBlogBlockingDTOMapper {
    @BeanMapping(resultType = BlogBlockingDTO.class)
    BlogBlockingDTO map(BlogBlocking blogBlocking);
}
