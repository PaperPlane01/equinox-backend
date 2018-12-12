package aphelion.mapper;

import aphelion.model.domain.BlogManager;
import aphelion.model.dto.BlogManagerDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;

@Mapper(uses = {
        UserToUserMinifiedDTOMapper.class,
        BlogToBlogMinifiedDTOMapper.class
})
public interface BlogManagerToBlogManagerDTOMapper {
    @BeanMapping(resultType = BlogManagerDTO.class)
    BlogManagerDTO map(BlogManager blogManager);
}
