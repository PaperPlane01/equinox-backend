package org.equinox.service;

import org.equinox.model.domain.ReportStatus;
import org.equinox.model.dto.BlogPostReportDTO;
import org.equinox.model.dto.CreateBlogPostReportDTO;
import org.equinox.model.dto.UpdateBlogPostReportDTO;

import java.util.List;

public interface BlogPostReportService {
    BlogPostReportDTO save(CreateBlogPostReportDTO createBlogPostReportDTO);
    BlogPostReportDTO update(Long id, UpdateBlogPostReportDTO updateBlogPostReportDTO);
    void delete(Long id);
    BlogPostReportDTO findById(Long id);
    List<BlogPostReportDTO> findAll(int page, int pageSize, String sortingDirection, String sortBy);
    List<BlogPostReportDTO> findByBlogPost(Long blogPostId, int page, int pageSize, String sortingDirection, String sortBy);
    List<BlogPostReportDTO> findByStatus(ReportStatus reportStatus, int page, int pageSize, String sortingDirection, String sortBy);
}
