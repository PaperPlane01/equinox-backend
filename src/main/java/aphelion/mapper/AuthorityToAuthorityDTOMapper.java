package aphelion.mapper;

import aphelion.model.domain.Authority;
import aphelion.model.dto.AuthorityDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;

@Mapper
public interface AuthorityToAuthorityDTOMapper {
    @BeanMapping(resultType = AuthorityDTO.class)
    AuthorityDTO map(Authority authority);
}
