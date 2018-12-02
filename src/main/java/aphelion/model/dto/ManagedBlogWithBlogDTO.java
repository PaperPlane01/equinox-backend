package aphelion.model.dto;

import aphelion.model.domain.BlogRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ManagedBlogWithBlogDTO {
    private Long blogManagerId;
    private BlogMinifiedDTO blog;
    private BlogRole blogRole;
    private Long userId;
}
