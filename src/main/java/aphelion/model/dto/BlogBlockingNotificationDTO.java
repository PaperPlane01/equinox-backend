package aphelion.model.dto;

import aphelion.model.domain.NotificationType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ApiModel(value = "Blog blocking notification", description = "Notification about blog blocking")
public class BlogBlockingNotificationDTO implements NotificationDTO {
    @ApiModelProperty("Id of notification")
    private Long id;
    @ApiModelProperty("Blog blocking")
    private BlogBlockingDTO blogBlocking;
    @ApiModelProperty("Notification type")
    private final NotificationType type = NotificationType.BLOG_BLOCKING;
    @ApiModelProperty("Has notification been read")
    private boolean read;
    @ApiModelProperty("Id of notification's recipient")
    private Long recipientId;
}
