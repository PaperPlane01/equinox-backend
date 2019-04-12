package aphelion.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import aphelion.model.domain.BlogRole;

@Builder
@NoArgsConstructor
@Data
@AllArgsConstructor
@ApiModel(value = "Managed blog", description = "Object representing managed blog")
public class ManagedBlogDTO {
    @ApiModelProperty("Id of blog")
    private Long blogId;
    @ApiModelProperty("Id of user")
    private Long userId;
    @ApiModelProperty("Role of blog manager")
    private BlogRole blogRole;
    @ApiModelProperty("Id of blog manager")
    private Long blogManagerId;
}
