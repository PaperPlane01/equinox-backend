package aphelion.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel("Subscription with user")
public class SubscriptionWithUserDTO {
    @ApiModelProperty("Id of subscription")
    private Long id;
    @ApiModelProperty("Subscribed user")
    private UserMinifiedDTO user;
    @ApiModelProperty("Id of blog")
    private Long blogId;
    @ApiModelProperty("Date of subscription")
    private Date subscriptionDate;
}
