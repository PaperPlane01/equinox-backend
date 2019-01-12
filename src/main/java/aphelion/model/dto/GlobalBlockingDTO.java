package aphelion.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class GlobalBlockingDTO {
    private Long id;
    private UserDTO blockedUser;
    private UserDTO blockedBy;
    private String reason;
    private Date startDate;
    private Date endDate;
}
