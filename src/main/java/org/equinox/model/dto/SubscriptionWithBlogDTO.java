package org.equinox.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionWithBlogDTO {
    private Long id;
    private BlogMinifiedDTO blog;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-mm-YYYY hh:MM:ss")
    private Date subscriptionDate;
}
