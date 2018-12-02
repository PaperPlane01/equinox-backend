package aphelion.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import aphelion.model.domain.User;
import aphelion.model.dto.UserDTO;

@Mapper(uses = AuthorityToAuthorityDTOMapper.class)
public interface UserToUserDTOMapper {
    @BeanMapping(resultType = UserDTO.class)
    @Mappings({
            @Mapping(source = "roles", target = "authorities"),
            @Mapping(source = "personalInformation.bio", target = "bio"),
            @Mapping(source = "personalInformation.email", target = "email"),
            @Mapping(source = "personalInformation.birthDate", target = "birthDate")
    })
    UserDTO map(User user);
}
