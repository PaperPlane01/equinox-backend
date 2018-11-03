package org.equinox.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.equinox.model.domain.GlobalBlocking;
import org.equinox.model.dto.GlobalBlockingDTO;

@Mapper(uses = UserToUserDTOMapper.class)
public interface GlobalBlockingToGlobalBlockingDTOMapper {
    @BeanMapping(resultType = GlobalBlockingDTO.class)
    GlobalBlockingDTO map(GlobalBlocking globalBlocking);
}
