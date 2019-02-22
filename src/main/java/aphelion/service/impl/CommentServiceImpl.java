package aphelion.service.impl;

import aphelion.annotation.CollectionArgument;
import aphelion.annotation.Page;
import aphelion.annotation.PageSize;
import aphelion.annotation.SortBy;
import aphelion.annotation.SortingDirection;
import aphelion.annotation.ValidateCollectionSize;
import aphelion.annotation.ValidatePaginationParameters;
import aphelion.exception.BlogPostNotFoundException;
import aphelion.exception.CommentNotFoundException;
import aphelion.mapper.CommentToCommentDTOMapper;
import aphelion.mapper.CreateCommentDTOToCommentMapper;
import aphelion.model.CommentsDisplayMode;
import aphelion.model.domain.BlogPost;
import aphelion.model.domain.Comment;
import aphelion.model.dto.CommentDTO;
import aphelion.model.dto.CreateCommentDTO;
import aphelion.model.dto.UpdateCommentDTO;
import aphelion.repository.BlogPostRepository;
import aphelion.repository.CommentRepository;
import aphelion.security.AuthenticationFacade;
import aphelion.service.CommentService;
import aphelion.service.TimeStampProvider;
import aphelion.util.SortingDirectionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneOffset;
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
    private final TimeStampProvider timeStampProvider;

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
        comment.setDeletedAt(Date.from(timeStampProvider.now().toInstant(ZoneOffset.UTC)));
        commentRepository.save(comment);
    }

    @Override
    public CommentDTO findById(Long id) throws CommentNotFoundException {
        return commentToCommentDTOMapper.map(findCommentById(id));
    }

    private Comment findCommentById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Could not find comment with " +
                        "given id " + id));
    }

    private BlogPost findBlogPostById(Long id) {
        return blogPostRepository.findById(id)
                .orElseThrow(() -> new BlogPostNotFoundException("Could not find blog post with " +
                        "given id " + id));
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
            return commentRepository.findRootByBlogPostOrderByNumberOfLikesAsc(blogPost, pageRequest);
        } else {
            return commentRepository.findRootByBlogOrderByNumberOfLikesDesc(blogPost, pageRequest);
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

    @Override
    @ValidateCollectionSize
    public void deleteMultiple(@CollectionArgument(maxSize = 30) List<Long> ids) {
        List<Comment> comments = commentRepository.findAllById(ids);
        comments.forEach(comment -> {
            comment.setDeleted(true);
            comment.setDeletedAt(Date.from(timeStampProvider.now().toInstant(ZoneOffset.UTC)));
            comment.setDeletedBy(authenticationFacade.getCurrentUser());
        });
        commentRepository.saveAll(comments);
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