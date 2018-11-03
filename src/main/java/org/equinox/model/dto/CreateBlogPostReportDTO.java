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
public class CreateBlogPostReportDTO {
    @NotNull
    private Long blogPostId;
    @NotNull
    private ReportReason reason;
    @Size(max = 80)
    private String description;
}
