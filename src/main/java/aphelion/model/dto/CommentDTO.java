package aphelion.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("Comment")
public class CommentDTO {
    @ApiModelProperty("Id of comment")
    private Long id;
    @ApiModelProperty("Id of blog")
    private Long blogId;
    @ApiModelProperty("Id of blog post")
    private Long blogPostId;
    @ApiModelProperty("Comment creation date")
    private Date createdAt;
    @ApiModelProperty("Author of comment")
    private UserMinifiedDTO author;
    @ApiModelProperty("Text of comments")
    private String content;
    @ApiModelProperty("Number of comment likes")
    private int numberOfLikes;
    @ApiModelProperty("Whether comment was liked by current user")
    private boolean likedByCurrentUser;
    @ApiModelProperty("Id of comment like")
    private Long likeId;
    @ApiModelProperty("Id of root comment")
    private Long rootCommentId;
    @ApiModelProperty("Id of referred comment")
    private Long referredCommentId;
    @ApiModelProperty("Replies to comments")
    private List<CommentDTO> replies = new ArrayList<>();
    @ApiModelProperty("Whether comment was deleted")
    private boolean deleted;
    @ApiModelProperty("Date when comment was deleted")
    private Date deletedAt;
    @ApiModelProperty("Id of user who deleted comment")
    private Long deletedByUserId;
}
