package aphelion.mapper;

import aphelion.model.domain.EmailConfirmation;
import aphelion.model.dto.EmailConfirmationDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface EmailConfirmationToEmailConfirmationDTOMapper {
    @BeanMapping(resultType = EmailConfirmationDTO.class)
    @Mapping(source = "user.id", target = "userId")
    EmailConfirmationDTO map(EmailConfirmation emailConfirmation);
}
