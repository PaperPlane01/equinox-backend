package org.equinox.service;

import org.equinox.model.domain.Comment;
import org.equinox.model.domain.User;
import org.equinox.model.dto.CommentLikeDTO;
import org.equinox.model.dto.CreateCommentLikeDTO;
import org.equinox.model.dto.UpdatedNumberOfCommentLikesDTO;

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
