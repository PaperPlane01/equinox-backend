package aphelion.model.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("Notification type")
public enum NotificationType {
    @ApiModelProperty("Notification about new blog post")
    NEW_BLOG_POST,
    @ApiModelProperty("Notification about new blog post like")
    NEW_BLOG_POST_LIKE,
    @ApiModelProperty("Notification about new comment reply <b>(currently not used)</b>")
    NEW_COMMENT_REPLY,
    @ApiModelProperty("Notification about new comment like")
    NEW_COMMENT_LIKE,
    @ApiModelProperty("Notification about blog blocking")
    BLOG_BLOCKING,
    @ApiModelProperty("Notification about global blocking")
    GLOBAL_BLOCKING
}
