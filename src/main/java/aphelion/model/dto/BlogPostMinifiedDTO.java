package aphelion.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogPostMinifiedDTO {
    private Long id;
    private BlogMinifiedDTO blog;
    private Map<Object, Object> content;
    private BlogPostPublisher publisher;
    private Date createdAt;
    private String title;
}
