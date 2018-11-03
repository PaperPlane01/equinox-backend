package org.equinox.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.equinox.model.domain.PersonalInformation;
import org.equinox.model.dto.PersonalInformationDTO;

@Mapper
public interface PersonalInformationToPersonalInformationDTOMapper {
    @BeanMapping(resultType = PersonalInformationDTO.class)
    @Mapping(source = "user.id", target = "userId")
    PersonalInformationDTO map(PersonalInformation personalInformation);
}
