package aphelion.model.dto;

import aphelion.model.domain.BlogRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BlogManagerDTO {
    private Long id;
    private UserMinifiedDTO user;
    private BlogMinifiedDTO blog;
    private BlogRole blogRole;
}
