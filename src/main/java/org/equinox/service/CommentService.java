package org.equinox.service;

import org.equinox.exception.BlogPostNotFoundException;
import org.equinox.exception.CommentNotFoundException;
import org.equinox.model.CommentsDisplayMode;
import org.equinox.model.dto.CommentDTO;
import org.equinox.model.dto.CreateCommentDTO;
import org.equinox.model.dto.UpdateCommentDTO;

import java.util.List;

public interface CommentService {
    CommentDTO save(CreateCommentDTO createCommentDTO) throws BlogPostNotFoundException, CommentNotFoundException;
    CommentDTO update(Long id, UpdateCommentDTO updateCommentDTO) throws CommentNotFoundException;
    void delete(Long id) throws CommentNotFoundException;
    CommentDTO findById(Long id) throws CommentNotFoundException;
    List<CommentDTO> findByBlogPost(Long blogPostId, int page, int pageSize, String sortingDirection, String sortBy, CommentsDisplayMode commentsDisplayMode);
    List<CommentDTO> findByRootComment(Long commentId, int page, int pageSize, String sortingDirection, String sortBy);
    CommentDTO restore(Long commentId) throws CommentNotFoundException;
}
