package aphelion.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import aphelion.model.domain.BlogManager;
import aphelion.model.dto.ManagedBlogDTO;

@Mapper
public interface BlogManagerToManagedBlogDTOMapper {
    @BeanMapping(resultType = ManagedBlogDTO.class)
    @Mappings({
            @Mapping(source = "blog.id", target = "blogId"),
            @Mapping(source = "user.id", target = "userId"),
            @Mapping(source = "id", target = "blogManagerId")
    })
    ManagedBlogDTO map(BlogManager blogManager);
}
