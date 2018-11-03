package org.equinox.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrentUserDTO {
    private Long id;
    private String displayedName;
    private List<AuthorityDTO> authorities;
    private List<Long> blockedInBlogs;
    private List<Long> ownedBlogs;
    private List<ManagedBlogDTO> managedBlogs;
    private String avatarUri;
    private boolean blockedGlobally;
}
