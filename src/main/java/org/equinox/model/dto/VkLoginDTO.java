package org.equinox.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VkLoginDTO {
    @NotNull
    private String vkId;
    @NotNull
    private String displayedName;
    private String avatarUri;
    @NotNull
    private String accessToken;
}
