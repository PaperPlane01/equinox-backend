package aphelion.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ManagedBlogWithUserDTO {
    private Long blogManagerId;
    private Long blogId;
    private UserMinifiedDTO user;
}
