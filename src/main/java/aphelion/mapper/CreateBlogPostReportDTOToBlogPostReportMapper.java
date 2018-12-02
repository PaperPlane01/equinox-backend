package aphelion.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import aphelion.exception.BlogPostNotFoundException;
import aphelion.model.domain.BlogPostReport;
import aphelion.model.domain.ReportStatus;
import aphelion.model.dto.CreateBlogPostReportDTO;
import aphelion.repository.BlogPostRepository;
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
