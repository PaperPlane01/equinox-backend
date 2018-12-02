package aphelion.model.dto;

import aphelion.model.domain.ReportReason;
import aphelion.model.domain.ReportStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
