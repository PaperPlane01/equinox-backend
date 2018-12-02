package aphelion.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import aphelion.model.domain.PersonalInformation;
import aphelion.model.dto.PersonalInformationDTO;

@Mapper
public interface PersonalInformationToPersonalInformationDTOMapper {
    @BeanMapping(resultType = PersonalInformationDTO.class)
    @Mapping(source = "user.id", target = "userId")
    PersonalInformationDTO map(PersonalInformation personalInformation);
}
