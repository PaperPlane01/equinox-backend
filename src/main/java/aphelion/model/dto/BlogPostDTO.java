package aphelion.model.dto;

import aphelion.model.domain.BlogPostPublisherType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BlogPostDTO {
    private Long id;
    private String title;
    private Map<Object, Object> content;
    private BlogPostPublisher publisher;
    private Long blogId;
    private Date createdAt;
    private Collection<TagDTO> tags;
    private Integer numberOfLikes;
    private Integer numberOfComments;
    private boolean likedByCurrentUser;
    private Long likeId;
    private int numberOfViews;
    private boolean deleted;
    private Long deletedByUserId;
    private BlogPostPublisherType publishedBy;
    private boolean canBeEdited;
    private boolean canBeDeleted;
    private boolean pinned;
    private Date pinDate;
}
