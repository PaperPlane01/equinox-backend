package aphelion.model.dto;

import aphelion.model.domain.ReportReason;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
