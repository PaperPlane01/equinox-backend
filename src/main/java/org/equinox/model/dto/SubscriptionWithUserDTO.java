package org.equinox.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubscriptionWithUserDTO {
    private Long id;
    private UserMinifiedDTO user;
    private Long blogId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-mm-YYYY hh:MM:ss")
    private Date subscriptionDate;
}
