package aphelion.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import aphelion.model.domain.BlogRole;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel(value = "Update blog manager request", description = "Request to update blog manager")
public class UpdateBlogManagerDTO {
    @NotNull
    @ApiModelProperty(value = "New blog role", required = true)
    private BlogRole blogRole;
}
