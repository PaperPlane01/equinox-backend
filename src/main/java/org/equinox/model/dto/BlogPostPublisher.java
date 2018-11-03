package org.equinox.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.equinox.model.domain.BlogPostPublisherType;

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
