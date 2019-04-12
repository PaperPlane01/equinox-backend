package aphelion.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ApiModel(value = "User minified", description = "Object representing a user, with few fields included")
public class UserMinifiedDTO {
    @ApiModelProperty("Id of user")
    private Long id;
    @ApiModelProperty("User's displayed name")
    private String displayedName;
    @ApiModelProperty("Link to user's avatar")
    private String avatarUri;
    @ApiModelProperty("User's letter avatar color")
    private String letterAvatarColor;
}
