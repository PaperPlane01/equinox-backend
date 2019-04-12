package aphelion.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
@ApiModel(value = "Blog minified", description = "Object representing a blog, with few fields included")
public class BlogMinifiedDTO {
    @ApiModelProperty("Id of blog")
    private Long id;
    @ApiModelProperty("Name of blog")
    private String name;
    @ApiModelProperty("Link to blog's avatar")
    private String avatarUri;
    @ApiModelProperty("Color of blog's letter avatar")
    private String letterAvatarColor;
}
