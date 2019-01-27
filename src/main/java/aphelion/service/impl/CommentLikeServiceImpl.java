package aphelion.service.impl;

import aphelion.annotation.Page;
import aphelion.annotation.PageSize;
import aphelion.annotation.SortBy;
import aphelion.annotation.SortingDirection;
import aphelion.annotation.ValidatePaginationParameters;
import aphelion.exception.CommentLikeNotFoundException;
import aphelion.exception.CommentNotFoundException;
import aphelion.exception.UserNotFoundException;
import aphelion.mapper.CommentLikeToCommentLikeDTOMapper;
import aphelion.mapper.CreateCommentLikeDTOToCommentLikeMapper;
import aphelion.model.domain.Comment;
import aphelion.model.domain.CommentLike;
import aphelion.model.domain.User;
import aphelion.model.dto.CommentLikeDTO;
import aphelion.model.dto.CreateCommentLikeDTO;
import aphelion.model.dto.UpdatedNumberOfCommentLikesDTO;
import aphelion.repository.CommentLikeRepository;
import aphelion.repository.CommentRepository;
import aphelion.repository.UserRepository;
import aphelion.service.CommentLikeService;
import aphelion.util.SortingDirectionUtils;
import lombok.RequiredArgsConstructor;
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
    public UpdatedNumberOfCommentLikesDTO save(CreateCommentLikeDTO createCommentLikeDTO) {
        CommentLike commentLike = createCommentLikeDTOToCommentLikeMapper.map(createCommentLikeDTO);
        commentLike = commentLikeRepository.save(commentLike);
        System.out.println("Saved comment like id: " + commentLike.getId());

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