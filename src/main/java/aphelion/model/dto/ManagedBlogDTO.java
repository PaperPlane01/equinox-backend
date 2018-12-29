package aphelion.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import aphelion.model.domain.BlogRole;

@Builder
@NoArgsConstructor
@Data
@AllArgsConstructor
public class ManagedBlogDTO {
    private Long blogId;
    private Long userId;
    private BlogRole blogRole;
    private Long blogManagerId;
}
