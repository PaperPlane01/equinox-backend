package aphelion.service;

import aphelion.model.domain.ReportStatus;
import aphelion.model.dto.CommentReportDTO;
import aphelion.model.dto.CreateCommentReportDTO;
import aphelion.model.dto.UpdateCommentReportDTO;

import java.util.List;

public interface CommentReportService {
    CommentReportDTO save(CreateCommentReportDTO createCommentReportDTO);
    CommentReportDTO update(Long id, UpdateCommentReportDTO updateCommentReportDTO);
    List<CommentReportDTO> updateMultiple(List<UpdateCommentReportDTO> updateCommentReportDTOs);
    void delete(Long id);
    CommentReportDTO findById(Long id);
    List<CommentReportDTO> findAll(int page, int pageSize, String sortingDirection, String sortBy);
    List<CommentReportDTO> findByComment(Long commentId, int page, int pageSize, String sortingDirection, String sortBy);
    List<CommentReportDTO> findByStatus(ReportStatus reportStatus, int page, int pageSize, String sortingDirection, String sortBy);
    List<CommentReportDTO> findByCommentAuthor(Long commentAuthorId, int page, int pageSize, String sortingDirection, String sortBy);
}
