package org.equinox.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.equinox.model.domain.User;
import org.equinox.model.dto.UserMinifiedDTO;

@Mapper
public interface UserToUserMinifiedDTOMapper {
    @BeanMapping(resultType = UserMinifiedDTO.class)
    UserMinifiedDTO map(User user);
}
