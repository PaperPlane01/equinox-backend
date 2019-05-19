package aphelion.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentMinifiedDTO {
    private Long id;
    private UserMinifiedDTO author;
    private Long blogPostId;
    private Date createdAt;
    private String content;
    private Long rootCommentId;
    private Long referredCommentId;
}
