package org.equinox.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.equinox.exception.BlogPostNotFoundException;
import org.equinox.model.domain.BlogPostReport;
import org.equinox.model.domain.ReportStatus;
import org.equinox.model.dto.CreateBlogPostReportDTO;
import org.equinox.repository.BlogPostRepository;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper
public abstract class CreateBlogPostReportDTOToBlogPostReportMapper {
    @Autowired
    private BlogPostRepository blogPostRepository;

    @BeforeMapping
    protected void setBlogPost(CreateBlogPostReportDTO createBlogPostReportDTO,
                               @MappingTarget BlogPostReport blogPostReport) {
        blogPostReport.setBlogPost(blogPostRepository
                .findById(createBlogPostReportDTO.getBlogPostId())
                .orElseThrow(() -> new BlogPostNotFoundException("Could not find blog post " +
                        "with given id " + createBlogPostReportDTO.getBlogPostId())));
        blogPostReport.setStatus(ReportStatus.NOT_VIEWED);
    }

    @BeanMapping(resultType = BlogPostReport.class)
    public abstract BlogPostReport map(CreateBlogPostReportDTO createBlogPostReportDTO);
}
