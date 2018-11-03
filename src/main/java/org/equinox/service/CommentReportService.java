package org.equinox.service;

import org.equinox.model.domain.ReportStatus;
import org.equinox.model.dto.CommentReportDTO;
import org.equinox.model.dto.CreateCommentReportDTO;
import org.equinox.model.dto.UpdateCommentReportDTO;

import java.util.List;

public interface CommentReportService {
    CommentReportDTO save(CreateCommentReportDTO createCommentReportDTO);
    CommentReportDTO update(Long id, UpdateCommentReportDTO updateCommentReportDTO);
    void delete(Long id);
    CommentReportDTO findById(Long id);
    List<CommentReportDTO> findAll(int page, int pageSize, String sortingDirection, String sortBy);
    List<CommentReportDTO> findByComment(Long commentId, int page, int pageSize, String sortingDirection, String sortBy);
    List<CommentReportDTO> findByStatus(ReportStatus reportStatus, int page, int pageSize, String sortingDirection, String sortBy);
    List<CommentReportDTO> findByCommentAuthor(Long commentAuthorId, int page, int pageSize, String sortingDirection, String sortBy);
}
