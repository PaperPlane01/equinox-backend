package aphelion.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "User", description = "Object representing user")
public class UserDTO {
    @ApiModelProperty("Id of user")
    private Long id;
    @ApiModelProperty("Displayed name of user")
    private String displayedName;
    @ApiModelProperty("Authorities")
    private List<AuthorityDTO> authorities;
    @ApiModelProperty("Link to avatar")
    private String avatarUri;
    @ApiModelProperty("Color of letter avatar")
    private String letterAvatarColor;
    @ApiModelProperty("Bio of user")
    private String bio;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-YYYY")
    @ApiModelProperty("Birth date")
    private Date birthDate;
    @ApiModelProperty("Email")
    private String email;
}