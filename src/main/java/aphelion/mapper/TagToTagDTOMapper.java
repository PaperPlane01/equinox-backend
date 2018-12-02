package aphelion.mapper;

import aphelion.model.domain.Tag;
import aphelion.model.dto.TagDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;

@Mapper
public interface TagToTagDTOMapper {
    @BeanMapping(resultType = TagDTO.class)
    TagDTO map(Tag tag);
}
