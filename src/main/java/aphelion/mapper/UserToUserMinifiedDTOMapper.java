package aphelion.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import aphelion.model.domain.User;
import aphelion.model.dto.UserMinifiedDTO;

@Mapper
public interface UserToUserMinifiedDTOMapper {
    @BeanMapping(resultType = UserMinifiedDTO.class)
    UserMinifiedDTO map(User user);
}
