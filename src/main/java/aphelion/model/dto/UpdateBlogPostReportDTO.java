package aphelion.model.dto;

import aphelion.model.domain.ReportStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBlogPostReportDTO {
    @NotNull
    private ReportStatus status;
}
