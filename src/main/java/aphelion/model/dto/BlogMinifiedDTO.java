package aphelion.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
public class BlogMinifiedDTO {
    private long id;
    private String name;
    private String avatarUri;
    private String letterAvatarColor;
}
