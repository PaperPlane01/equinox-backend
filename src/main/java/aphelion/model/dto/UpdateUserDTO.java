package aphelion.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDTO {
    @Size(max = 70)
    private String displayedName;
    private String avatarUri;

    @Size(max = 120)
    private String bio;

    @Past
    private Date birthDate;
}
