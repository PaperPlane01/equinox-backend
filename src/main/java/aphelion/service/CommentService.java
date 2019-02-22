package aphelion.service;

import aphelion.exception.BlogPostNotFoundException;
import aphelion.exception.CommentNotFoundException;
import aphelion.model.CommentsDisplayMode;
import aphelion.model.dto.CommentDTO;
import aphelion.model.dto.CreateCommentDTO;
import aphelion.model.dto.UpdateCommentDTO;

import java.util.List;

public interface CommentService {
    CommentDTO save(CreateCommentDTO createCommentDTO) throws BlogPostNotFoundException, CommentNotFoundException;
    CommentDTO update(Long id, UpdateCommentDTO updateCommentDTO) throws CommentNotFoundException;
    void delete(Long id) throws CommentNotFoundException;
    CommentDTO findById(Long id) throws CommentNotFoundException;
    List<CommentDTO> findByBlogPost(Long blogPostId, int page, int pageSize, String sortingDirection, String sortBy, CommentsDisplayMode commentsDisplayMode);
    List<CommentDTO> findByRootComment(Long commentId, int page, int pageSize, String sortingDirection, String sortBy);
    CommentDTO restore(Long commentId) throws CommentNotFoundException;
    void deleteMultiple(List<Long> ids);
}
