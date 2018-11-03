package org.equinox.service.impl;

import lombok.RequiredArgsConstructor;
import org.equinox.mapper.BlogPostToBlogPostDTOMapper;
import org.equinox.mapper.BlogPostToBlogPostMinifiedDTOMapper;
import org.equinox.mapper.CreateBlogPostDTOToBlogPostMapper;
import org.equinox.model.domain.Blog;
import org.equinox.model.domain.BlogPost;
import org.equinox.model.domain.NotificationType;
import org.equinox.model.domain.Tag;
import org.equinox.model.domain.User;
import org.equinox.model.dto.BlogPostDTO;
import org.equinox.model.dto.BlogPostMinifiedDTO;
import org.equinox.model.dto.CreateBlogPostDTO;
import org.equinox.model.dto.UpdateBlogPostDTO;
import org.equinox.service.BlogPostService;
import org.equinox.util.SortingDirectionUtils;
import org.equinox.annotation.NotifySubscribers;
import org.equinox.annotation.Page;
import org.equinox.annotation.PageSize;
import org.equinox.annotation.SortBy;
import org.equinox.annotation.SortingDirection;
import org.equinox.annotation.ValidatePaginationParameters;
import org.equinox.exception.BlogNotFoundException;
import org.equinox.exception.BlogPostNotFoundException;
import org.equinox.repository.BlogPostRepository;
import org.equinox.repository.BlogRepository;
import org.equinox.repository.TagRepository;
import org.equinox.security.AuthenticationFacade;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BlogPostServiceImpl implements BlogPostService {
    private final BlogPostRepository blogPostRepository;
    private final BlogRepository blogRepository;
    private final TagRepository tagRepository;
    private final AuthenticationFacade authenticationFacade;
    private final BlogPostToBlogPostDTOMapper blogPostToBlogPostDTOMapper;
    private final BlogPostToBlogPostMinifiedDTOMapper blogPostToBlogPostMinifiedDTOMapper;
    private final CreateBlogPostDTOToBlogPostMapper createBlogPostDTOToBlogPostMapper;

    @Override
    @NotifySubscribers(type = NotificationType.NEW_BLOG_POST)
    public BlogPostDTO save(CreateBlogPostDTO createBlogPostDTO) {
        BlogPost blogPost = createBlogPostDTOToBlogPostMapper.map(createBlogPostDTO);
        blogPost = blogPostRepository.save(blogPost);
        return blogPostToBlogPostDTOMapper.map(blogPost);
    }

    @Override
    public BlogPostDTO update(Long id, UpdateBlogPostDTO updateBlogPostDTO) {
        BlogPost blogPost = findBlogPostById(id);
        blogPost.setTags(updateBlogPostDTO.getTags().stream().map(Tag::new).map(tagRepository::save).collect(Collectors.toList()));
        blogPost.setTitle(updateBlogPostDTO.getTitle());
        blogPost.setContent(updateBlogPostDTO.getContent());
        blogPost = blogPostRepository.save(blogPost);
        return blogPostToBlogPostDTOMapper.map(blogPost);
    }

    @Override
    public BlogPostDTO findById(Long id) {
        return blogPostToBlogPostDTOMapper.map(findBlogPostById(id));
    }

    @Override
    public BlogPostDTO findDeletedById(Long id) {
        BlogPost blogPost = findDeletedBlogPostById(id);
        return blogPostToBlogPostDTOMapper.map(blogPost);
    }

    private BlogPost findDeletedBlogPostById(Long id) {
        return blogPostRepository.findByIdAndDeleted(id, true)
                .orElseThrow(() -> new BlogPostNotFoundException("Could not find deleted blog post with given id " + id));
    }

    @Override
    @ValidatePaginationParameters
    public List<BlogPostMinifiedDTO> findDeletedByCurrentUserInBlog(Long blogId,
                                                                    @Page int page,
                                                                    @PageSize(max = 20) int pageSize,
                                                                    @SortingDirection String sortingDirection,
                                                                    @SortBy(allowed = "id") String sortBy) {
        User currentUser = authenticationFacade.getCurrentUser();
        Blog blog = findBlogById(blogId);
        Sort.Direction direction = SortingDirectionUtils.convertFromString(sortingDirection);
        PageRequest pageRequest = PageRequest.of(page, pageSize, direction, sortBy);

        List<BlogPost> deletedBlogPosts = blogPostRepository
                .findByBlogAndDeletedAndDeletedBy(blog, true, currentUser, pageRequest);

        return deletedBlogPosts.stream()
                .map(blogPostToBlogPostMinifiedDTOMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        BlogPost blogPost = findBlogPostById(id);
        blogPost.setDeleted(true);
        blogPost.setDeletedAt(Date.from(Instant.now()));
        blogPost.setDeletedBy(authenticationFacade.getCurrentUser());
        blogPostRepository.save(blogPost);
    }

    @Override
    public BlogPostDTO restore(Long id) {
        BlogPost blogPost = findDeletedBlogPostById(id);
        blogPost.setDeleted(false);
        blogPost.setDeletedAt(null);
        blogPost.setDeletedBy(null);
        blogPost = blogPostRepository.save(blogPost);
        return blogPostToBlogPostDTOMapper.map(blogPost);
    }

    private BlogPost findBlogPostById(Long id) {
        return blogPostRepository.findById(id)
                .orElseThrow(() -> new BlogPostNotFoundException("Blog post with given id " + id + " could not be found."));
    }

    @Override
    @ValidatePaginationParameters
    public List<BlogPostDTO> findByBlog(Long blogId,
                                        @Page int page,
                                        @PageSize(max = 50) int pageSize,
                                        @SortingDirection String sortingDirection,
                                        @SortBy(allowed = {
                                                "id", "createdAt"
                                        }) String sortBy) {
        Blog blog = findBlogById(blogId);
        Sort.Direction direction = SortingDirectionUtils.convertFromString(sortingDirection);
        PageRequest pageRequest = PageRequest.of(page, pageSize, direction, sortBy);
        return blogPostRepository.findByBlog(blog, pageRequest)
                .stream()
                .map(blogPostToBlogPostDTOMapper::map)
                .collect(Collectors.toList());
    }

    private Blog findBlogById(Long blogId) {
        return blogRepository.findById(blogId)
                .orElseThrow(() -> new BlogNotFoundException("Blog with given id " + blogId + " could not be found."));
    }
}