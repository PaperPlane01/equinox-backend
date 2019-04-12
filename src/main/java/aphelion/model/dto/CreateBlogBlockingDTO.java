package aphelion.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Create blog blocking request", description = "Object containing data about created blog blocking")
public class CreateBlogBlockingDTO {
    @NotNull
    @ApiModelProperty("Id of blocked user")
    private Long blockedUserId;
    @NotNull
    @ApiModelProperty("Blog on which this blocking will be applied")
    private Long blogId;
    @NotNull
    @Future
    @ApiModelProperty("End date of blocking")
    private Date endDate;
    @ApiModelProperty("Reason of blocking")
    private String reason;
}
