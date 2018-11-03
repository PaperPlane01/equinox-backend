package org.equinox.service.impl;

import lombok.RequiredArgsConstructor;
import org.equinox.exception.CommentLikeNotFoundException;
import org.equinox.exception.UserNotFoundException;
import org.equinox.annotation.NotifySubscribers;
import org.equinox.annotation.Page;
import org.equinox.annotation.PageSize;
import org.equinox.annotation.SortBy;
import org.equinox.annotation.SortingDirection;
import org.equinox.annotation.ValidatePaginationParameters;
import org.equinox.exception.CommentNotFoundException;
import org.equinox.mapper.CommentLikeToCommentLikeDTOMapper;
import org.equinox.mapper.CreateCommentLikeDTOToCommentLikeMapper;
import org.equinox.model.domain.Comment;
import org.equinox.model.domain.CommentLike;
import org.equinox.model.domain.NotificationType;
import org.equinox.model.domain.User;
import org.equinox.model.dto.CommentLikeDTO;
import org.equinox.model.dto.CreateCommentLikeDTO;
import org.equinox.model.dto.UpdatedNumberOfCommentLikesDTO;
import org.equinox.repository.CommentLikeRepository;
import org.equinox.repository.CommentRepository;
import org.equinox.repository.UserRepository;
import org.equinox.service.CommentLikeService;
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
public class CommentLikeServiceImpl implements CommentLikeService {
    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CommentLikeToCommentLikeDTOMapper commentLikeToCommentLikeDTOMapper;
    private final CreateCommentLikeDTOToCommentLikeMapper createCommentLikeDTOToCommentLikeMapper;

    @Override
    @NotifySubscribers(type = NotificationType.NEW_COMMENT_LIKE)
    public UpdatedNumberOfCommentLikesDTO save(CreateCommentLikeDTO createCommentLikeDTO) {
        CommentLike commentLike = createCommentLikeDTOToCommentLikeMapper.map(createCommentLikeDTO);
        commentLike = commentLikeRepository.save(commentLike);

        return new UpdatedNumberOfCommentLikesDTO(commentLike.getId(),
                countByComment(commentLike.getComment()));
    }

    @Override
    public UpdatedNumberOfCommentLikesDTO delete(Long id) {
        CommentLike commentLike = findCommentLikeById(id);
        commentLike.getComment().removeLike(commentLike);
        commentLikeRepository.delete(commentLike);
        return new UpdatedNumberOfCommentLikesDTO(null, countByComment(commentLike.getComment()));
    }

    @Override
    public CommentLikeDTO findById(Long id) {
        CommentLike commentLike = findCommentLikeById(id);
        return commentLikeToCommentLikeDTOMapper.map(commentLike);
    }

    private CommentLike findCommentLikeById(Long id) {
        return commentLikeRepository.findById(id)
                .orElseThrow(() -> new CommentLikeNotFoundException("Comment like with given id "
                        + id + " could not be found."));
    }

    private int countByComment(Comment comment) {
        return commentLikeRepository.countAllByComment(comment);
    }

    @Override
    public boolean isCommentLikedByUser(Long commentId, Long userId) {
        return isCommentLikedByUser(findCommentById(commentId), findUserById(userId));
    }

    @Override
    public boolean isCommentLikedByUser(Comment comment, User user) {
        return commentLikeRepository.findByUserAndComment(user, comment).isPresent();
    }

    @Override
    @ValidatePaginationParameters
    public List<CommentLikeDTO> findByComment(Long commentId,
                                              @Page int page,
                                              @PageSize(max = 150) int pageSize,
                                              @SortingDirection String sortingDirection,
                                              @SortBy(allowed = {"id", "createdAt"}) String sortBy) {
        Comment comment = findCommentById(commentId);
        Sort.Direction direction = SortingDirectionUtils.convertFromString(sortingDirection);
        PageRequest pageRequest = PageRequest.of(page, pageSize, direction, sortBy);
        return commentLikeRepository.findByComment(comment, pageRequest)
                .stream()
                .map(commentLikeToCommentLikeDTOMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    @ValidatePaginationParameters
    public List<CommentLikeDTO> findByUser(Long userId,
                                           @Page int page,
                                           @PageSize(max = 150) int pageSize,
                                           @SortingDirection String sortingDirection,
                                           @SortBy(allowed = {"id", "createdAt"}) String sortBy) {
        User user = findUserById(userId);
        Sort.Direction direction = SortingDirectionUtils.convertFromString(sortingDirection);
        PageRequest pageRequest = PageRequest.of(page, pageSize, direction, sortBy);
        return commentLikeRepository.findByUser(user, pageRequest)
                .stream()
                .map(commentLikeToCommentLikeDTOMapper::map)
                .collect(Collectors.toList());
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with given id "
                        + userId + " could not be found."));
    }

    private Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment with given id "
                        + commentId + " could not be found."));
    }

    @Override
    public int countByComment(Long commentId) {
        return commentLikeRepository.countAllByComment(findCommentById(commentId));
    }
}