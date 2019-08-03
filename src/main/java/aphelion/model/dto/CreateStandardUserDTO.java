package aphelion.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateStandardUserDTO {
    @NotBlank
    @Size(min = 3, max = 25)
    private String loginUsername;

    @Size(min = 3, max = 25)
    private String displayedUsername;

    @NotBlank
    @Size(min = 3, max = 25)
    private String password;

    @NotBlank
    private String repeatedPassword;

    @Email
    @Nullable
    private String email;

    private String confirmationEmailLanguage;
}
