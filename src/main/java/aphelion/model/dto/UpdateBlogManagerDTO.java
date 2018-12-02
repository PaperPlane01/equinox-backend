package aphelion.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import aphelion.model.domain.BlogRole;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateBlogManagerDTO {
    private BlogRole blogRole;
}
