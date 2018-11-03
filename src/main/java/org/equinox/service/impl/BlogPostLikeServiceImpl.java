package org.equinox.service.impl;

import lombok.RequiredArgsConstructor;
import org.equinox.exception.UserNotFoundException;
import org.equinox.mapper.BlogPostLikeToBlogPostLikeDTOMapper;
import org.equinox.mapper.CreateBlogPostLikeDTOToBlogPostLikeMapper;
import org.equinox.model.domain.User;
import org.equinox.model.dto.BlogPostLikeDTO;
import org.equinox.model.dto.CreateBlogPostLikeDTO;
import org.equinox.service.BlogPostLikeService;
import org.equinox.annotation.Page;
import org.equinox.annotation.PageSize;
import org.equinox.annotation.SortBy;
import org.equinox.annotation.SortingDirection;
import org.equinox.annotation.ValidatePaginationParameters;
import org.equinox.exception.BlogPostLikeNotFoundException;
import org.equinox.exception.BlogPostNotFoundException;
import org.equinox.model.domain.BlogPost;
import org.equinox.model.domain.BlogPostLike;
import org.equinox.model.dto.UpdatedNumberOfBlogPostLikesDTO;
import org.equinox.repository.BlogPostLikeRepository;
import org.equinox.repository.BlogPostRepository;
import org.equinox.repository.UserRepository;
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
public class BlogPostLikeServiceImpl implements BlogPostLikeService {
    private final BlogPostLikeRepository blogPostLikeRepository;
    private final UserRepository userRepository;
    private final BlogPostRepository blogPostRepository;
    private final BlogPostLikeToBlogPostLikeDTOMapper blogPostLikeToBlogPostLikeDTOMapper;
    private final CreateBlogPostLikeDTOToBlogPostLikeMapper createBlogPostLikeDTOToBlogPostLikeMapper;

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
    public List<BlogPostLikeDTO> findByUser(Long userId, int page, int pageSize, String sortingDirection, String sortBy) {
        User user = findUserById(userId);
        return findByUser(user, page, pageSize, sortingDirection, sortBy)
                .stream()
                .map(blogPostLikeToBlogPostLikeDTOMapper::map)
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