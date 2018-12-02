package aphelion.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import aphelion.model.domain.BlogPostReport;
import aphelion.model.dto.BlogPostReportDTO;

@Mapper
public interface BlogPostReportToBlogPostReportDTOMapper {
    @BeanMapping(resultType = BlogPostReportDTO.class)
    BlogPostReportDTO map(BlogPostReport blogPostReport);
}
