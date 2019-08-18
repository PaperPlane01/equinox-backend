package aphelion.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateEmailDTO {
    @Email
    private String email;
    private String emailConfirmationLanguage;
}
