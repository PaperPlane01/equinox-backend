package aphelion.model.dto;

import aphelion.model.domain.BlogRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ManagedBlogWithUserDTO {
    private Long blogManagerId;
    private Long blogId;
    private BlogRole blogRole;
    private UserMinifiedDTO user;
}
