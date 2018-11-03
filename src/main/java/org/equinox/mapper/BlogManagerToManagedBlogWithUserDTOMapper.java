package org.equinox.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.equinox.model.domain.BlogManager;
import org.equinox.model.dto.ManagedBlogWithUserDTO;

@Mapper(uses = UserToUserMinifiedDTOMapper.class)
public interface BlogManagerToManagedBlogWithUserDTOMapper {
    @BeanMapping(resultType = ManagedBlogWithUserDTO.class)
    @Mappings({
            @Mapping(source = "blog.id", target = "blogId"),
            @Mapping(source = "id", target = "blogManagerId")
    })
    ManagedBlogWithUserDTO map(BlogManager blogManager);
}
