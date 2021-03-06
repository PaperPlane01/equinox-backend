package aphelion.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrentUserFullProfileDTO {
    private Long id;
    private String displayedName;
    private String avatarUri;
    private String bio;
    private String email;
    private Date birthDate;
    private List<AuthorityDTO> authorities;
    private List<Long> blockedInBlogs;
    private List<Long> ownedBlogs;
    private List<ManagedBlogDTO> managedBlogs;
    private boolean blockedGlobally;
}
