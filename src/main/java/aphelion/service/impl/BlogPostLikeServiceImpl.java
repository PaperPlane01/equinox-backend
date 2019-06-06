package aphelion.service.impl;

import aphelion.annotation.Page;
import aphelion.annotation.PageSize;
import aphelion.annotation.SortBy;
import aphelion.annotation.SortingDirection;
import aphelion.annotation.ValidatePaginationParameters;
import aphelion.exception.BlogPostLikeNotFoundException;
import aphelion.exception.BlogPostNotFoundException;
import aphelion.exception.UserNotFoundException;
import aphelion.mapper.BlogPostLikeToBlogPostLikeDTOMapper;
import aphelion.mapper.BlogPostLikeToBlogPostLikeWithBlogPostDTOMapper;
import aphelion.mapper.CreateBlogPostLikeDTOToBlogPostLikeMapper;
import aphelion.model.domain.BlogPost;
import aphelion.model.domain.BlogPostLike;
import aphelion.model.domain.User;
import aphelion.model.dto.BlogPostLikeDTO;
import aphelion.model.dto.BlogPostLikeWithBlogPostDTO;
import aphelion.model.dto.CreateBlogPostLikeDTO;
import aphelion.model.dto.UpdatedNumberOfBlogPostLikesDTO;
import aphelion.repository.BlogPostLikeRepository;
import aphelion.repository.BlogPostRepository;
import aphelion.repository.UserRepository;
import aphelion.util.SortingDirectionUtils;
import lombok.RequiredArgsConstructor;
import aphelion.service.BlogPostLikeService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BlogPostLikeServiceImpl implements BlogPostLikeService {
    private final BlogPostLikeRepository blogPostLikeRepository;
    private final UserRepository userRepository;
    private final BlogPostRepository blogPostRepository;
    private final BlogPostLikeToBlogPostLikeDTOMapper blogPostLikeToBlogPostLikeDTOMapper;
    private final CreateBlogPostLikeDTOToBlogPostLikeMapper createBlogPostLikeDTOToBlogPostLikeMapper;
    private final BlogPostLikeToBlogPostLikeWithBlogPostDTOMapper blogPostLikeToBlogPostLikeWithBlogPostDTOMapper;

    @Override
    public UpdatedNumberOfBlogPostLikesDTO save(CreateBlogPostLikeDTO createBlogPostLikeDTO) {
        BlogPostLike blogPostLike = createBlogPostLikeDTOToBlogPostLikeMapper
                .map(createBlogPostLikeDTO);
        blogPostLike = blogPostLikeRepository.save(blogPostLike);
        int numberOfLikes = countByBlogPost(blogPostLike.getBlogPost());
        return new UpdatedNumberOfBlogPostLikesDTO(blogPostLike.getId(), numberOfLikes);
    }

    @Override
    public UpdatedNumberOfBlogPostLikesDTO delete(Long id) {
        BlogPostLike blogPostLike = findBlogPostLikeById(id);
        BlogPost blogPost = blogPostLike.getBlogPost();
        blogPost.removeBlogPostLike(blogPostLike);
        blogPostLikeRepository.delete(blogPostLike);

        UpdatedNumberOfBlogPostLikesDTO updatedNumberOfBlogPostLikesDTO = new UpdatedNumberOfBlogPostLikesDTO();
        updatedNumberOfBlogPostLikesDTO.setUpdatedNumberOfLikes(blogPost.getLikes().size());
        return updatedNumberOfBlogPostLikesDTO;
    }

    private BlogPostLike findBlogPostLikeById(Long id) {
        return blogPostLikeRepository.findById(id)
                .orElseThrow(() -> new BlogPostLikeNotFoundException("Blog post like with given id "
                        + id + " could not be found"));
    }

    @Override
    @ValidatePaginationParameters
    public List<BlogPostLikeDTO> findByBlogPost(Long blogPostId,
                                                @Page int page,
                                                @PageSize(max = 200) int pageSize,
                                                @SortingDirection String sortingDirection,
                                                @SortBy(allowed = "id") String sortBy) {
        BlogPost blogPost = findBlogPostById(blogPostId);
        return findByBlogPost(blogPost, page, pageSize, sortingDirection, sortBy)
                .stream()
                .map(blogPostLikeToBlogPostLikeDTOMapper::map)
                .collect(Collectors.toList());
    }

    private List<BlogPostLike> findByBlogPost(BlogPost blogPost, int page, int pageSize, String sortingDirection, String sortBy) {
        Sort.Direction direction = SortingDirectionUtils.convertFromString(sortingDirection);
        PageRequest pageRequest = PageRequest.of(page, pageSize, direction, sortBy);
        return blogPostLikeRepository.findByBlogPost(blogPost, pageRequest);
    }

    @Override
    @ValidatePaginationParameters
    public List<BlogPostLikeWithBlogPostDTO> findByUser(
            Long userId,
            @Page int page,
            @PageSize(max = 50) int pageSize,
            @SortingDirection String sortingDirection,
            @SortBy(allowed = "id") String sortBy) {
        User user = findUserById(userId);
        return findByUser(user, page, pageSize, sortingDirection, sortBy)
                .stream()
                .map(blogPostLikeToBlogPostLikeWithBlogPostDTOMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public BlogPostLikeDTO findById(Long id) {
        return blogPostLikeToBlogPostLikeDTOMapper.map(findBlogPostLikeById(id));
    }

    private List<BlogPostLike> findByUser(User user, int page, int pageSize, String sortingDirection, String sortBy) {
        Sort.Direction direction = SortingDirectionUtils.convertFromString(sortingDirection);
        PageRequest pageRequest = PageRequest.of(page, pageSize, direction, sortBy);
        return blogPostLikeRepository.findByUser(user, pageRequest);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with given id "
                        + userId + " could not be found."));
    }

    private BlogPost findBlogPostById(Long blogPostId) {
        return blogPostRepository.findById(blogPostId)
                .orElseThrow(() -> new BlogPostNotFoundException("Blog post with given id "
                        + blogPostId + " could not be found."));
    }

    @Override
    public int countByBlogPost(Long blogPostId) {
        BlogPost blogPost = findBlogPostById(blogPostId);
        return countByBlogPost(blogPost);
    }

    private int countByBlogPost(BlogPost blogPost) {
        return blogPostLikeRepository.countByBlogPost(blogPost);
    }

    @Override
    public boolean isBlogPostLikedByUser(BlogPost blogPost, User user) {
        return blogPostLikeRepository.findByBlogPostAndUser(blogPost, user).isPresent();
    }
}