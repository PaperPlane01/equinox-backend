package aphelion.model.dto;

import aphelion.model.domain.BlogPostPublisherType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBlogPostDTO {
    private String title;
    private Long blogId;
    private Map<Object, Object> content;
    private List<String> tags;
    private BlogPostPublisherType publishedBy;
}
