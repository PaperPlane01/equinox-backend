package aphelion.model.dto;

import aphelion.model.domain.ReportReason;
import aphelion.model.domain.ReportStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class BlogPostReportDTO {
    private Long id;
    private BlogPostMinifiedWithAuthorDTO blogPost;
    private String description;
    private ReportStatus status;
    private ReportReason reason;
}
