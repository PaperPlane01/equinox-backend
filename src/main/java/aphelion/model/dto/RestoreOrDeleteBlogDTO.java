package aphelion.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel(
        value = "Request to delete or restore blog",
        description = "If <code>deleted</code> is <code>true</code>, " +
                "the blog will be removed. Otherwise, it will bee restored"
)
public class RestoreOrDeleteBlogDTO {
    @ApiModelProperty("Specifies whether blog should be deleted or restored")
    private Boolean deleted;
}
