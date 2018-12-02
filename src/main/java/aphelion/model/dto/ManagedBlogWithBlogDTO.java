package aphelion.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ManagedBlogWithBlogDTO {
    private Long blogManagerId;
    private BlogMinifiedDTO blog;
    private Long userId;
}
