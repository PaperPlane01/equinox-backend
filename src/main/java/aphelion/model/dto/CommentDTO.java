package aphelion.model.dto;

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
public class CommentDTO {
    private Long id;
    private Long blogId;
    private Long blogPostId;
    private Date createdAt;
    private UserDTO author;
    private String content;
    private int numberOfLikes;
    private boolean likedByCurrentUser;
    private Long likeId;
    private Long rootCommentId;
    private Long referredCommentId;
    private List<CommentDTO> replies = new ArrayList<>();
    private boolean deleted;
    private Date deletedAt;
    private Long deletedByUserId;
}
