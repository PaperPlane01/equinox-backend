package org.equinox.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.equinox.model.domain.ReportReason;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentReportDTO {
    @NotNull
    private Long commentId;

    @NotNull
    @Size(min = 1, max = 100)
    private String description;

    @NotNull
    private ReportReason reason;
}
