package org.equinox.repository;

import org.equinox.model.domain.BlogPost;
import org.equinox.model.domain.BlogPostReport;
import org.equinox.model.domain.ReportStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BlogPostReportRepository extends JpaRepository<BlogPostReport, Long> {
    Optional<BlogPostReport> findById(Long id);
    BlogPostReport save(BlogPostReport blogPostReport);
    void delete(BlogPostReport blogPostReport);
    List<BlogPostReport> findBy(Pageable pageable);
    List<BlogPostReport> findByBlogPost(BlogPost blogPost, Pageable pageable);
    List<BlogPostReport> findByStatus(ReportStatus reportStatus, Pageable pageable);
}
