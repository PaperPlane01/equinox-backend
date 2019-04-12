package aphelion.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Blog blocking", description = "Object representing blog blocking")
public class BlogBlockingDTO {
    @ApiModelProperty("Id of blog blocking")
    private Long id;
    @ApiModelProperty("Blocked user")
    private UserMinifiedDTO blockedUser;
    @ApiModelProperty("User who created blog blocking")
    private UserMinifiedDTO blockedBy;
    @ApiModelProperty("Blog in which this blocking has been applied")
    private BlogMinifiedDTO blog;
    @ApiModelProperty("Reason of blocking")
    private String reason;
    @ApiModelProperty("Date when blog blocking has been created")
    private Date startDate;
    @ApiModelProperty("Date when blog blocking will expire")
    private Date endDate;
}
