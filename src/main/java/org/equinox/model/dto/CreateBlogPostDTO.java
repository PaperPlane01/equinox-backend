package org.equinox.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.equinox.model.domain.BlogPostPublisherType;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBlogPostDTO {
    private String title;
    private Long blogId;
    private Map<Object, Object> content;
    private List<String> tags;
    private BlogPostPublisherType publishedBy;
}
