package org.equinox.service.impl;

import lombok.RequiredArgsConstructor;
import org.equinox.exception.CommentReportNotFoundException;
import org.equinox.exception.UserNotFoundException;
import org.equinox.annotation.Page;
import org.equinox.annotation.PageSize;
import org.equinox.annotation.SortBy;
import org.equinox.annotation.SortingDirection;
import org.equinox.annotation.ValidatePaginationParameters;
import org.equinox.exception.CommentNotFoundException;
import org.equinox.mapper.CommentReportToCommentReportDTOMapper;
import org.equinox.mapper.CreateCommentReportDTOToCommentReportMapper;
import org.equinox.model.domain.Comment;
import org.equinox.model.domain.CommentReport;
import org.equinox.model.domain.ReportStatus;
import org.equinox.model.domain.User;
import org.equinox.model.dto.CommentReportDTO;
import org.equinox.model.dto.CreateCommentReportDTO;
import org.equinox.model.dto.UpdateCommentReportDTO;
import org.equinox.repository.CommentReportRepository;
import org.equinox.repository.CommentRepository;
import org.equinox.repository.UserRepository;
import org.equinox.service.CommentReportService;
import org.equinox.util.SortingDirectionUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentReportServiceImpl implements CommentReportService {
    private final CommentReportRepository commentReportRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CommentReportToCommentReportDTOMapper commentReportToCommentReportDTOMapper;
    private final CreateCommentReportDTOToCommentReportMapper createCommentReportDTOToCommentReportMapper;

    @Override
    public CommentReportDTO save(CreateCommentReportDTO createCommentReportDTO) {
        CommentReport commentReport = createCommentReportDTOToCommentReportMapper
                .map(createCommentReportDTO);
        commentReport = commentReportRepository.save(commentReport);
        return commentReportToCommentReportDTOMapper.map(commentReport);
    }

    @Override
    public CommentReportDTO update(Long id, UpdateCommentReportDTO updateCommentReportDTO) {
        CommentReport commentReport = findCommentReportById(id);
        commentReport.setStatus(ReportStatus.fromString(updateCommentReportDTO.getStatus()));
        commentReport = commentReportRepository.save(commentReport);
        return commentReportToCommentReportDTOMapper.map(commentReport);
    }

    @Override
    public void delete(Long id) {
        CommentReport commentReport = findCommentReportById(id);
        commentReportRepository.delete(commentReport);
    }

    @Override
    public CommentReportDTO findById(Long id) {
        return commentReportToCommentReportDTOMapper.map(findCommentReportById(id));
    }

    private CommentReport findCommentReportById(Long id) {
        return commentReportRepository.findById(id)
                .orElseThrow(() -> new CommentReportNotFoundException("Comment report " +
                        "with given id " + id + " could not be found."));
    }

    @Override
    @ValidatePaginationParameters
    public List<CommentReportDTO> findAll(@Page int page,
                                          @PageSize(max = 150) int pageSize,
                                          @SortingDirection String sortingDirection,
                                          @SortBy(allowed = "id") String sortBy) {
        Sort.Direction direction = SortingDirectionUtils.convertFromString(sortingDirection);
        PageRequest pageRequest = PageRequest.of(page, pageSize, direction, sortBy);
        return commentReportRepository.findAllBy(pageRequest)
                .stream()
                .map(commentReportToCommentReportDTOMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    @ValidatePaginationParameters
    public List<CommentReportDTO> findByComment(Long commentId,
                                                @Page int page,
                                                @PageSize(max = 150) int pageSize,
                                                @SortingDirection String sortingDirection,
                                                @SortBy(allowed = "id") String sortBy) {
        Comment comment = findCommentById(commentId);
        Sort.Direction direction = SortingDirectionUtils.convertFromString(sortingDirection);
        PageRequest pageRequest = PageRequest.of(page, pageSize, direction, sortBy);
        return commentReportRepository.findByComment(comment, pageRequest)
                .stream()
                .map(commentReportToCommentReportDTOMapper::map)
                .collect(Collectors.toList());
    }

    private Comment findCommentById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment with given id "
                        + id + " could not be found."));
    }

    @Override
    @ValidatePaginationParameters
    public List<CommentReportDTO> findByStatus(ReportStatus reportStatus,
                                               @Page int page,
                                               @PageSize(max = 150) int pageSize,
                                               @SortingDirection String sortingDirection,
                                               @SortBy(allowed = "id") String sortBy) {
        Sort.Direction direction = SortingDirectionUtils.convertFromString(sortingDirection);
        PageRequest pageRequest = PageRequest.of(page, pageSize, direction, sortBy);
        return commentReportRepository.findByStatus(reportStatus, pageRequest)
                .stream()
                .map(commentReportToCommentReportDTOMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    @ValidatePaginationParameters
    public List<CommentReportDTO> findByCommentAuthor(Long commentAuthorId,
                                                      @Page int page,
                                                      @PageSize(max = 150) int pageSize,
                                                      @SortingDirection String sortingDirection,
                                                      @SortBy(allowed = "id") String sortBy) {
        User user = findUserById(commentAuthorId);
        Sort.Direction direction = SortingDirectionUtils.convertFromString(sortingDirection);
        PageRequest pageRequest = PageRequest.of(page, pageSize, direction, sortBy);
        return commentReportRepository.findByCommentAuthor(user, pageRequest)
                .stream()
                .map(commentReportToCommentReportDTOMapper::map)
                .collect(Collectors.toList());
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with given id "
                        + id + " could not be found."));
    }
}
