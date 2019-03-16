package aphelion.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlogPostMinifiedWithAuthorDTO {
    private Long id;
    private BlogMinifiedDTO blog;
    private Map<Object, Object> content;
    private UserMinifiedDTO author;
    private Date createdAt;
    private String title;
}
