package org.equinox.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BlogMinifiedDTO {
    private long id;
    private String name;
    private String avatarUri;
    private String letterAvatarColor;
}
