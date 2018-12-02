package aphelion.mapper;

import aphelion.model.domain.BlogManager;
import aphelion.model.dto.ManagedBlogWithUserDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(uses = UserToUserMinifiedDTOMapper.class)
public interface BlogManagerToManagedBlogWithUserDTOMapper {
    @BeanMapping(resultType = ManagedBlogWithUserDTO.class)
    @Mappings({
            @Mapping(source = "blog.id", target = "blogId"),
            @Mapping(source = "id", target = "blogManagerId")
    })
    ManagedBlogWithUserDTO map(BlogManager blogManager);
}
