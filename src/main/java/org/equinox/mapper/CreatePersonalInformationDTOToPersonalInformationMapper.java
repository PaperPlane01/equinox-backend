package org.equinox.mapper;

import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.equinox.model.domain.PersonalInformation;
import org.equinox.model.dto.CreatePersonalInformationDTO;
import org.equinox.security.AuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper
public abstract class CreatePersonalInformationDTOToPersonalInformationMapper {
    @Autowired
    private AuthenticationFacade authenticationFacade;

    public abstract PersonalInformation map(CreatePersonalInformationDTO createPersonalInformationDTO);

    @BeforeMapping
    protected void setUser(CreatePersonalInformationDTO createPersonalInformationDTO,
                           @MappingTarget PersonalInformation personalInformation) {
        personalInformation.setDeleted(false);
        personalInformation.setUser(authenticationFacade.getCurrentUser());
    }
}
