package aphelion.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubscriptionDTO {
    private Long id;
    private UserMinifiedDTO user;
    private BlogMinifiedDTO blog;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-mm-YYYY hh:MM:ss")
    private Date date;
}
