package aphelion.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserWithoutProfileDTO {
    private Long id;
    private String displayedUsername;
    private String avatarUri;
    private Collection<AuthorityDTO> authorities;
}
