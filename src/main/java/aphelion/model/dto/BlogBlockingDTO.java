package aphelion.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogBlockingDTO {
    private Long id;
    private UserDTO blockedUser;
    private UserDTO blockedBy;
    private BlogMinifiedDTO blog;
    private String reason;
    private Date startDate;
    private Date endDate;
}
