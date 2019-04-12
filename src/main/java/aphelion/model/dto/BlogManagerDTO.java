package aphelion.model.dto;

import aphelion.model.domain.BlogRole;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel(value = "Blog manager", description = "Object representing blog manager")
public class BlogManagerDTO {
    @ApiModelProperty("Id of manager")
    private Long id;
    @ApiModelProperty("User who manages the blog")
    private UserMinifiedDTO user;
    @ApiModelProperty("Blog which is managed")
    private BlogMinifiedDTO blog;
    @ApiModelProperty("Role of manager")
    private BlogRole blogRole;
}
