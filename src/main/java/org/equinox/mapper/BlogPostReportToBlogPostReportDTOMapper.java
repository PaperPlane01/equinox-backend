package org.equinox.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.equinox.model.domain.BlogPostReport;
import org.equinox.model.dto.BlogPostReportDTO;

@Mapper
public interface BlogPostReportToBlogPostReportDTOMapper {
    @BeanMapping(resultType = BlogPostReportDTO.class)
    BlogPostReportDTO map(BlogPostReport blogPostReport);
}
