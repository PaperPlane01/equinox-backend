package aphelion.model.dto;

import aphelion.model.domain.BlogPostPublisherType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogPostPublisher {
    private Long id;
    private String displayedName;
    private String avatarUri;
    private String letterAvatarColor;
    private BlogPostPublisherType type;
}
