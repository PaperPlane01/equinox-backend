package org.equinox.mapper;

import org.equinox.model.domain.Authority;
import org.equinox.model.dto.AuthorityDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;

@Mapper
public interface AuthorityToAuthorityDTOMapper {
    @BeanMapping(resultType = AuthorityDTO.class)
    AuthorityDTO map(Authority authority);
}
