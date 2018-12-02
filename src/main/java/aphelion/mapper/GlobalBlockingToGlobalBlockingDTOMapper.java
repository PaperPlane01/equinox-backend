package aphelion.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import aphelion.model.domain.GlobalBlocking;
import aphelion.model.dto.GlobalBlockingDTO;

@Mapper(uses = UserToUserDTOMapper.class)
public interface GlobalBlockingToGlobalBlockingDTOMapper {
    @BeanMapping(resultType = GlobalBlockingDTO.class)
    GlobalBlockingDTO map(GlobalBlocking globalBlocking);
}
