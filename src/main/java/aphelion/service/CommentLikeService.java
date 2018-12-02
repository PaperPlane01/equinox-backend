package aphelion.service;

import aphelion.model.domain.Comment;
import aphelion.model.domain.User;
import aphelion.model.dto.CommentLikeDTO;
import aphelion.model.dto.CreateCommentLikeDTO;
import aphelion.model.dto.UpdatedNumberOfCommentLikesDTO;

import java.util.List;

public interface CommentLikeService {
    UpdatedNumberOfCommentLikesDTO save(CreateCommentLikeDTO createCommentLikeDTO);
    UpdatedNumberOfCommentLikesDTO delete(Long id);
    CommentLikeDTO findById(Long id);
    boolean isCommentLikedByUser(Long commentId, Long userId);
    boolean isCommentLikedByUser(Comment comment, User user);
    List<CommentLikeDTO> findByComment(Long commentId, int page, int pageSize, String sortingDirection, String sortBy);
    List<CommentLikeDTO> findByUser(Long userId, int page, int pageSize, String sortingDirection, String sortBy);
    int countByComment(Long commentId);
}
