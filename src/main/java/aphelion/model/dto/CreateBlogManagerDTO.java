package aphelion.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import aphelion.model.domain.BlogRole;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel(value = "Create blog manager request", description = "Request to create new blog manager")
public class CreateBlogManagerDTO {
    @NotNull
    @ApiModelProperty(value = "Id of user", required = true)
    private Long userId;
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private Long blogId;

    @NotNull
    @ApiModelProperty(value = "Role of manager", required = true)
    private BlogRole blogRole;
}
