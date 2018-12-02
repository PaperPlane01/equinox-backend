package aphelion.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import aphelion.model.domain.BlogRole;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class ManagedBlogDTO {
    private Long blogId;
    private Long userId;
    private BlogRole blogRole;
    private Long blogManagerId;
}
