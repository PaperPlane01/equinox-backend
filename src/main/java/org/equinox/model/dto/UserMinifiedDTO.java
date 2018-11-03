package org.equinox.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserMinifiedDTO {
    private Long id;
    private String displayedName;
    private String avatarUri;
    private String letterAvatarColor;
}
