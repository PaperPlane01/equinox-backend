package org.equinox.service.impl;

import lombok.RequiredArgsConstructor;
import org.equinox.annotation.Page;
import org.equinox.annotation.PageSize;
import org.equinox.annotation.SortBy;
import org.equinox.annotation.SortingDirection;
import org.equinox.annotation.ValidatePaginationParameters;
import org.equinox.exception.BlogPostNotFoundException;
import org.equinox.exception.CommentNotFoundException;
import org.equinox.mapper.CommentToCommentDTOMapper;
import org.equinox.mapper.CreateCommentDTOToCommentMapper;
import org.equinox.model.CommentsDisplayMode;
import org.equinox.model.domain.BlogPost;
import org.equinox.model.domain.Comment;
import org.equinox.model.dto.CommentDTO;
import org.equinox.model.dto.CreateCommentDTO;
import org.equinox.model.dto.UpdateCommentDTO;
import org.equinox.repository.BlogPostRepository;
import org.equinox.repository.CommentRepository;
import org.equinox.security.AuthenticationFacade;
import org.equinox.service.CommentService;
import org.equinox.util.SortingDirectionUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final BlogPostRepository blogPostRepository;
    private final CommentToCommentDTOMapper commentToCommentDTOMapper;
    private final CreateCommentDTOToCommentMapper createCommentDTOToCommentMapper;
    private final AuthenticationFacade authenticationFacade;

    @Override
    public CommentDTO save(CreateCommentDTO createCommentDTO) throws BlogPostNotFoundException, CommentNotFoundException {
        Comment comment = createCommentDTOToCommentMapper.map(createCommentDTO);
        return commentToCommentDTOMapper.map(commentRepository.save(comment));
    }

    @Override
    public CommentDTO update(Long id, UpdateCommentDTO updateCommentDTO) throws CommentNotFoundException {
        Comment comment = findCommentById(id);
        comment.setContent(updateCommentDTO.getContent());
        return commentToCommentDTOMapper.map(commentRepository.save(comment));
    }

    @Override
    public void delete(Long id) throws CommentNotFoundException {
        Comment comment = findCommentById(id);
        comment.setDeleted(true);
        comment.setDeletedBy(authenticationFacade.getCurrentUser());
        comment.setDeletedAt(Date.from(Instant.now()));
        commentRepository.save(comment);
    }

    @Override
    public CommentDTO findById(Long id) throws CommentNotFoundException {
        return commentToCommentDTOMapper.map(findCommentById(id));
    }

    private Comment findCommentById(Long id) {
        return commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
    }

    private BlogPost findBlogPostById(Long id) {
        return blogPostRepository.findById(id).orElseThrow(BlogPostNotFoundException::new);
    }

    private List<Comment> findAllByRootComment(Comment rootComment) {
        return commentRepository.findAllByRootComment(rootComment);
    }

    @Override
    @ValidatePaginationParameters
    public List<CommentDTO> findByBlogPost(Long blogPostId,
                                           @Page int page,
                                           @PageSize(max = 200) int pageSize,
                                           @SortingDirection String sortingDirection,
                                           @SortBy(allowed = {
                                                   "id", "createdAt", "numberOfLikes"
                                           }) String sortBy, CommentsDisplayMode commentsDisplayMode) {
        BlogPost blogPost = findBlogPostById(blogPostId);
        if (commentsDisplayMode.equals(CommentsDisplayMode.FLAT)) {
            return findByBlogPost(blogPost, page, pageSize, sortingDirection, sortBy)
                    .stream()
                    .map(commentToCommentDTOMapper::map)
                    .collect(Collectors.toList());
        } else {
            List<Comment> rootComments = findRootCommentsByBlogPost(blogPost, page, pageSize, sortingDirection, sortBy);
            return rootComments.stream()
                    .map(comment -> commentToCommentDTOMapper.map(comment, findAllByRootComment(comment)))
                    .collect(Collectors.toList());
        }
    }

    private List<Comment> findByBlogPost(BlogPost blogPost, int page, int pageSize, String sortingDirection, String sortBy) {
        if ("numberOfLikes".equalsIgnoreCase(sortBy)) {
            return findByBlogPostSortByNumberOfLikes(blogPost, page, pageSize, sortingDirection);
        } else {
            Sort.Direction direction = SortingDirectionUtils.convertFromString(sortingDirection);
            PageRequest pageRequest = PageRequest.of(page, pageSize, direction, sortBy);
            return commentRepository.findByBlogPost(blogPost, pageRequest);
        }
    }

    private List<Comment> findByBlogPostSortByNumberOfLikes(BlogPost blogPost, int page, int pageSize, String sortingDirection) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        if ("asc".equalsIgnoreCase(sortingDirection)) {
            return commentRepository.findByBlogPostOrderByNumberOfLikes(blogPost, pageRequest);
        } else {
            return commentRepository.findByBlogPostOrderByNumberOfLikesDesc(blogPost, pageRequest);
        }
    }

    private List<Comment> findRootCommentsByBlogPost(BlogPost blogPost, int page, int pageSize, String sortingDirection,
                                                     String sortBy) {
        if ("numberOfLikes".equalsIgnoreCase(sortBy)) {
            return findRootCommentsByBlogPostSortByNumberOfLikes(blogPost, page, pageSize, sortingDirection);
        } else {
            Sort.Direction direction = SortingDirectionUtils.convertFromString(sortingDirection);
            PageRequest pageRequest = PageRequest.of(page, pageSize, direction, sortBy);
            return commentRepository.findByBlogPostAndRoot(blogPost, true, pageRequest);
        }
    }

    private List<Comment> findRootCommentsByBlogPostSortByNumberOfLikes(BlogPost blogPost, int page, int pageSize,
                                                                        String sortingDirection) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);

        if ("asc".equalsIgnoreCase(sortingDirection)) {
            return commentRepository.findByBlogPostAndRootOrderByNumberOfLikesAsc(blogPost, true, pageRequest);
        } else {
            return commentRepository.findByBlogPostAndRootOrderByNumberOfLikesDesc(blogPost, true, pageRequest);
        }
    }

    @Override
    @ValidatePaginationParameters
    public List<CommentDTO> findByRootComment(Long commentId,
                                              @Page int page,
                                              @PageSize(max = 200) int pageSize,
                                              @SortingDirection String sortingDirection,
                                              @SortBy(allowed = {
                                                      "id", "createdAt", "numberOfLikes"
                                              }) String sortBy) {
        return findByRootComment(findCommentById(commentId), page, pageSize, sortingDirection, sortBy)
                .stream()
                .map(commentToCommentDTOMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDTO restore(Long commentId) {
        Comment comment = findCommentById(commentId);
        comment.setDeleted(false);
        comment = commentRepository.save(comment);
        return commentToCommentDTOMapper.map(comment);
    }

    private List<Comment> findByRootComment(Comment rootComment, int page, int pageSize, String sortingDirection,
                                            String sortBy) {
        if ("numberOfLikes".equalsIgnoreCase(sortBy)) {
            return findByRootCommentSortByNumberOfLikes(rootComment, page, pageSize, sortingDirection);
        } else {
            Sort.Direction direction = SortingDirectionUtils.convertFromString(sortingDirection);
            PageRequest pageRequest = PageRequest.of(page, pageSize, direction, sortBy);
            return commentRepository.findByRootComment(rootComment, pageRequest);
        }
    }

    private List<Comment> findByRootCommentSortByNumberOfLikes(Comment rootComment, int page, int pageSize, String sortingDirection) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);

        if ("asc".equalsIgnoreCase(sortingDirection)) {
            return commentRepository.findByRootCommentOrderByNumberOfLikes(rootComment, pageRequest);
        } else {
            return commentRepository.findByRootCommentOrderByNumberOfLikesDesc(rootComment, pageRequest);
        }
    }
}