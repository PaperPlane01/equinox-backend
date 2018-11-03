package org.equinox.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateGlobalBlockingDTO {
    @NotNull
    private Long blockedUserId;

    @Size(max = 100)
    private String reason;
    @NotNull
    private Date endDate;
}
