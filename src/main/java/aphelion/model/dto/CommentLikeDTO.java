package aphelion.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class CommentLikeDTO {
    private Long id;
    private UserDTO user;
    private Long commentId;
}
