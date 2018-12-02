package aphelion.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import aphelion.model.domain.BlogManager;
import aphelion.model.dto.ManagedBlogWithBlogDTO;

@Mapper(uses = BlogToBlogMinifiedDTOMapper.class)
public interface BlogManagerToManagedBlogWithBlogDTOMapper {
    @BeanMapping(resultType = ManagedBlogWithBlogDTO.class)
    @Mappings({
            @Mapping(source = "user.id", target = "userId"),
            @Mapping(source = "id", target = "blogManagerId")
    })
    ManagedBlogWithBlogDTO map(BlogManager blogManager);
}
