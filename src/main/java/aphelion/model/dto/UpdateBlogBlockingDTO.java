package aphelion.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Update blog blocking request", description = "Request to update blog blocking")
public class UpdateBlogBlockingDTO {
    @ApiModelProperty(value = "New reason", required = true)
    private String reason;
    @NotNull
    @ApiModelProperty(value = "New end date", required = true)
    private Date endDate;
}
