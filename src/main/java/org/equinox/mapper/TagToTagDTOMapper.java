package org.equinox.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.equinox.model.domain.Tag;
import org.equinox.model.dto.TagDTO;

@Mapper
public interface TagToTagDTOMapper {
    @BeanMapping(resultType = TagDTO.class)
    TagDTO map(Tag tag);
}
