package org.equinox.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
    private UserDTO author;
    private String content;
    private int numberOfLikes;
    private boolean likedByCurrentUser;
    private Long likeId;
    private Long rootCommentId;
    private Long referredCommentId;
    private List<CommentDTO> replies;
    private boolean deleted;
    private Date deletedAt;
    private Long deletedByUserId;
}
