package aphelion.service.impl;

import aphelion.annotation.CollectionArgument;
import aphelion.annotation.Page;
import aphelion.annotation.PageSize;
import aphelion.annotation.SortBy;
import aphelion.annotation.SortingDirection;
import aphelion.annotation.ValidateCollectionSize;
import aphelion.annotation.ValidatePaginationParameters;
import aphelion.exception.CommentNotFoundException;
import aphelion.exception.CommentReportNotFoundException;
import aphelion.exception.UserNotFoundException;
import aphelion.mapper.CommentReportToCommentReportDTOMapper;
import aphelion.mapper.CreateCommentReportDTOToCommentReportMapper;
import aphelion.model.domain.Comment;
import aphelion.model.domain.CommentReport;
import aphelion.model.domain.ReportStatus;
import aphelion.model.domain.User;
import aphelion.model.dto.CommentReportDTO;
import aphelion.model.dto.CreateCommentReportDTO;
import aphelion.model.dto.UpdateCommentReportDTO;
import aphelion.repository.CommentReportRepository;
import aphelion.repository.CommentRepository;
import aphelion.repository.UserRepository;
import aphelion.util.SortingDirectionUtils;
import lombok.RequiredArgsConstructor;
import aphelion.service.CommentReportService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    @ValidateCollectionSize
    public List<CommentReportDTO> updateMultiple(
            @CollectionArgument(maxSize = 30) List<UpdateCommentReportDTO> updateCommentReportDTOs) {
        List<Long> ids = updateCommentReportDTOs.stream()
                .map(UpdateCommentReportDTO::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        Map<Long, UpdateCommentReportDTO> updateCommentReportDTOMap = new HashMap<>();
        updateCommentReportDTOs.forEach(updateCommentReportDTO -> {
            if (updateCommentReportDTO.getId() != null) {
                updateCommentReportDTOMap.put(updateCommentReportDTO.getId(), updateCommentReportDTO);
            }
        });
        List<CommentReport> commentReports = commentReportRepository.findAllById(ids);
        commentReports.forEach(report -> {
            ReportStatus reportStatus = ReportStatus
                    .fromString(updateCommentReportDTOMap.get(report.getId()).getStatus());
            report.setStatus(reportStatus);
        });
        return commentReportRepository.saveAll(commentReports)
                .stream()
                .map(commentReportToCommentReportDTOMapper::map)
                .collect(Collectors.toList());
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
