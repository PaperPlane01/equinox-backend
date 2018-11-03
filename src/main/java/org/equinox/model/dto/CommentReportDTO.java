package org.equinox.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.equinox.model.domain.ReportReason;
import org.equinox.model.domain.ReportStatus;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class CommentReportDTO {
    private Long id;
    private CommentMinifiedDTO comment;
    private ReportStatus status;
    private ReportReason reason;
    private String content;
}
