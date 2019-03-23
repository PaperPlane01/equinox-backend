package aphelion.service;

import aphelion.model.domain.ReportStatus;
import aphelion.model.dto.BlogPostReportDTO;
import aphelion.model.dto.CreateBlogPostReportDTO;
import aphelion.model.dto.UpdateBlogPostReportDTO;

import java.util.List;

public interface BlogPostReportService {
    BlogPostReportDTO save(CreateBlogPostReportDTO createBlogPostReportDTO);
    BlogPostReportDTO update(Long id, UpdateBlogPostReportDTO updateBlogPostReportDTO);
    void delete(Long id);
    BlogPostReportDTO findById(Long id);
    List<BlogPostReportDTO> findAll(int page, int pageSize, String sortingDirection, String sortBy);
    List<BlogPostReportDTO> findByBlogPost(Long blogPostId, int page, int pageSize, String sortingDirection, String sortBy);
    List<BlogPostReportDTO> findByStatus(ReportStatus reportStatus, int page, int pageSize, String sortingDirection, String sortBy);
    List<BlogPostReportDTO> updateMultiple(List<UpdateBlogPostReportDTO> updateBlogPostReportDTOList);
}
