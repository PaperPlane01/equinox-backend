package org.equinox.service.impl;

import lombok.RequiredArgsConstructor;
import org.equinox.mapper.CommentToCommentDTOMapper;
import org.equinox.mapper.CreateCommentDTOToCommentMapper;
import org.equinox.model.domain.BlogPost;
import org.equinox.model.domain.Comment;
import org.equinox.model.CommentsDisplayMode;
import org.equinox.model.dto.CommentDTO;
import org.equinox.model.dto.CreateCommentDTO;
import org.equinox.model.dto.UpdateCommentDTO;
import org.equinox.service.CommentService;
import org.equinox.util.SortingDirectionUtils;
import org.equinox.annotation.Page;
import org.equinox.annotation.PageSize;
import org.equinox.annotation.SortBy;
import org.equinox.annotation.SortingDirection;
import org.equinox.annotation.ValidatePaginationParameters;
import org.equinox.exception.BlogPostNotFoundException;
import org.equinox.exception.BlogPostReportNotFoundException;
import org.equinox.exception.CommentNotFoundException;
import org.equinox.repository.BlogPostRepository;
import org.equinox.repository.CommentRepository;
import org.equinox.security.AuthenticationFacade;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        return blogPostRepository.findById(id).orElseThrow(BlogPostReportNotFoundException::new);
    }

    @Override
    @ValidatePaginationParameters
    public List<CommentDTO> findAllByBlogPost(Long blogPostId,
                                              @SortingDirection String sortingDirection,
                                              @SortBy(allowed = {
                                                      "id", "createdAt", "numberOfLikes"
                                              }) String sortBy,
                                              CommentsDisplayMode commentsDisplayMode) throws BlogPostNotFoundException {
        BlogPost blogPost = findBlogPostById(blogPostId);
        if (commentsDisplayMode.equals(CommentsDisplayMode.FLAT)) {
            return findCommentsByBlogPost(blogPost, sortingDirection, sortBy)
                    .stream()
                    .map(commentToCommentDTOMapper::map)
                    .collect(Collectors.toList());
        } else {
            List<Comment> comments = findAllRootCommentsByBlogPost(blogPost);
            return comments
                    .stream()
                    .map(comment -> commentToCommentDTOMapper.map(comment, findAllByRootComment(comment)))
                    .collect(Collectors.toList());
        }
    }

    private List<Comment> findCommentsByBlogPost(BlogPost blogPost, String sortingDirection, String sortBy) {
        if (sortBy.equalsIgnoreCase("numberOfLikes")) {
            return findCommentsByBlogPostSortByNumberOfLikes(blogPost, sortingDirection);
        } else {
            return findCommentsByBlogPostSortById(blogPost, sortingDirection);
        }
    }

    private List<Comment> findCommentsByBlogPostSortByNumberOfLikes(BlogPost blogPost, String sortingDirection) {

        if (sortingDirection.equalsIgnoreCase("asc")) {
            return commentRepository.findAllByBlogPostOrderByNumberOfLikesAsc(blogPost);
        } else {
            return commentRepository.findAllByBlogPostOrderByNumberOfLikesDesc(blogPost);
        }
    }

    private List<Comment> findCommentsByBlogPostSortById(BlogPost blogPost, String sortingDirection) {
        if (sortingDirection.equalsIgnoreCase("asc")) {
            return commentRepository.findAllByBlogPost(blogPost);
        } else {
            return commentRepository.findAllByBlogPostOrderByIdDesc(blogPost);
        }
    }

    private List<Comment> findAllRootCommentsByBlogPost(BlogPost blogPost) {
        return commentRepository.findAllByBlogPostAndRoot(blogPost, true);
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
        if (sortBy.equalsIgnoreCase("numberOfLikes")) {
            return findByBlogPostSortByNumberOfLikes(blogPost, page, pageSize, sortingDirection);
        } else {
            return findByBlogPostSortById(blogPost, page, pageSize, sortingDirection);
        }
    }

    private List<Comment> findByBlogPostSortByNumberOfLikes(BlogPost blogPost, int page, int pageSize, String sortingDirection) {
        Sort.Direction direction = SortingDirectionUtils.convertFromString(sortingDirection);
        PageRequest pageRequest = PageRequest.of(page, pageSize, direction);
        return commentRepository.findByBlogPostOrderByNumberOfLikes(blogPost, pageRequest);
    }

    private List<Comment> findByBlogPostSortById(BlogPost blogPost, int page, int pageSize, String sortingDirection) {
        Sort.Direction direction = SortingDirectionUtils.convertFromString(sortingDirection);
        PageRequest pageRequest = PageRequest.of(page, pageSize, direction, "id");
        return commentRepository.findByBlogPost(blogPost, pageRequest);
    }

    private List<Comment> findRootCommentsByBlogPost(BlogPost blogPost, int page, int pageSize, String sortingDirection,
                                                     String sortBy) {
        if (sortBy.equalsIgnoreCase("numberOfLikes")) {
            return findRootCommentsByBlogPostSortByNumberOfLikes(blogPost, page, pageSize, sortingDirection);
        } else {
            return findRootCommentsByBlogPostSortById(blogPost, page, pageSize, sortingDirection);
        }
    }

    private List<Comment> findRootCommentsByBlogPostSortByNumberOfLikes(BlogPost blogPost, int page, int pageSize,
                                                                        String sortingDirection) {
        Sort.Direction direction = SortingDirectionUtils.convertFromString(sortingDirection);
        PageRequest pageRequest = PageRequest.of(page, pageSize, direction);
        return commentRepository.findByBlogPostAndRootOrderByNumberOfLikes(blogPost, true, pageRequest);
    }

    private List<Comment> findRootCommentsByBlogPostSortById(BlogPost blogPost, int page, int pageSize,
                                                             String sortingDirection) {
        Sort.Direction direction = SortingDirectionUtils.convertFromString(sortingDirection);
        PageRequest pageRequest = PageRequest.of(page, pageSize, direction, "id");
        return commentRepository.findByBlogPostAndRoot(blogPost, true, pageRequest);
    }

    @Override
    @ValidatePaginationParameters
    public List<CommentDTO> findAllByRootComment(Long commentId,
                                                 @SortingDirection String sortingDirection,
                                                 @SortBy(allowed = {
                                                         "id", "createdAt", "numberOfLikes"
                                                 }) String sortBy) {
        Comment rootComment = findCommentById(commentId);

        return findAllByRootComment(rootComment, sortingDirection, sortBy)
                .stream()
                .map(commentToCommentDTOMapper::map)
                .collect(Collectors.toList());
    }

    private List<Comment> findAllByRootComment(Comment rootComment, String sortingDirection, String sortBy) {
        if (sortBy.equalsIgnoreCase("numberOfLikes")) {
            return findAllByRootCommentSortByNumberOfLikes(rootComment, sortingDirection);
        } else {
            return findAllByRootCommentSortById(rootComment, sortingDirection);
        }
    }

    private List<Comment> findAllByRootCommentSortByNumberOfLikes(Comment rootComment, String sortingDirection) {
        if (sortingDirection.equalsIgnoreCase("asc")) {
            return commentRepository.findAllByRootCommentOrderByNumberOfLikesAsc(rootComment);
        } else {
            return commentRepository.findAllByRootCommentOrderByNumberOfLikesDesc(rootComment);
        }
    }

    private List<Comment> findAllByRootCommentSortById(Comment rootComment, String sortingDirection) {
        if (sortingDirection.equalsIgnoreCase("asc")) {
            return commentRepository.findAllByRootComment(rootComment);
        } else {
            return commentRepository.findAllByRootCommentOrderByIdDesc(rootComment);
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
        Sort.Direction direction = SortingDirectionUtils.convertFromString(sortingDirection);

        if (sortBy.equalsIgnoreCase("numberOfLikes")) {
            PageRequest pageRequest = PageRequest.of(page, pageSize, direction);
            return findByRootCommentSortByNumberOfLikes(rootComment, pageRequest);
        } else {
            PageRequest pageRequest = PageRequest.of(page, pageSize, direction, sortBy);
            return findByRootComment(rootComment, pageRequest);
        }
    }

    private List<Comment> findByRootCommentSortByNumberOfLikes(Comment rootComment, Pageable pageable) {
        return commentRepository.findByRootCommentOrderByNumberOfLikes(rootComment, pageable);
    }

    private List<Comment> findByRootComment(Comment rootComment, Pageable pageable) {
        return commentRepository.findByRootComment(rootComment, pageable);
    }
}