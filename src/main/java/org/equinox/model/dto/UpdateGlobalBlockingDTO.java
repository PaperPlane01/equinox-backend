package org.equinox.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateGlobalBlockingDTO {
    @NotNull
    private Long id;
    @NotNull
    private String reason;
    @NotNull
    private Date endDate;
}
