package aphelion.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlogPostLikeWithBlogPostDTO {
    private Long id;
    private BlogPostDTO blogPost;
    private Long userId;
}
