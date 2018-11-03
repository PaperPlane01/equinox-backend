package org.equinox.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.equinox.model.domain.ReportReason;
import org.equinox.model.domain.ReportStatus;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class BlogPostReportDTO {
    private Long id;
    private Long blogPostId;
    private String description;
    private ReportStatus status;
    private ReportReason reason;
}
