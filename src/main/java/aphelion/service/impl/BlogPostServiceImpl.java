package aphelion.service.impl;

import aphelion.annotation.CollectionArgument;
import aphelion.annotation.Page;
import aphelion.annotation.PageSize;
import aphelion.annotation.SortBy;
import aphelion.annotation.SortingDirection;
import aphelion.annotation.ValidateCollectionSize;
import aphelion.annotation.ValidatePaginationParameters;
import aphelion.exception.BlogNotFoundException;
import aphelion.exception.BlogPostNotFoundException;
import aphelion.exception.PinnedBlogPostsLimitHasBeenReachedException;
import aphelion.mapper.BlogPostToBlogPostDTOMapper;
import aphelion.mapper.BlogPostToBlogPostMinifiedDTOMapper;
import aphelion.mapper.CreateBlogPostDTOToBlogPostMapper;
import aphelion.mapper.UserToUserDTOMapper;
import aphelion.model.domain.Blog;
import aphelion.model.domain.BlogPost;
import aphelion.model.domain.Notification;
import aphelion.model.domain.NotificationType;
import aphelion.model.domain.Tag;
import aphelion.model.domain.User;
import aphelion.model.dto.BlogPostDTO;
import aphelion.model.dto.BlogPostMinifiedDTO;
import aphelion.model.dto.CreateBlogPostDTO;
import aphelion.model.dto.UpdateBlogPostDTO;
import aphelion.model.dto.UserDTO;
import aphelion.repository.BlogPostRepository;
import aphelion.repository.BlogRepository;
import aphelion.repository.NotificationRepository;
import aphelion.repository.TagRepository;
import aphelion.security.AuthenticationFacade;
import aphelion.service.BlogPostContentValidationService;
import aphelion.service.BlogPostService;
import aphelion.service.TimeStampProvider;
import aphelion.util.SortingDirectionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BlogPostServiceImpl implements BlogPostService {
    private final BlogPostRepository blogPostRepository;
    private final BlogRepository blogRepository;
    private final TagRepository tagRepository;
    private final NotificationRepository notificationRepository;
    private final AuthenticationFacade authenticationFacade;
    private final BlogPostToBlogPostMinifiedDTOMapper blogPostToBlogPostMinifiedDTOMapper;
    private final CreateBlogPostDTOToBlogPostMapper createBlogPostDTOToBlogPostMapper;
    private final UserToUserDTOMapper userToUserDTOMapper;
    private final BlogPostContentValidationService blogPostContentValidationService;
    private final TimeStampProvider timeStampProvider;
    private BlogPostToBlogPostDTOMapper blogPostToBlogPostDTOMapper;

    @Autowired
    public void setBlogPostToBlogPostDTOMapper(BlogPostToBlogPostDTOMapper blogPostToBlogPostDTOMapper) {
        this.blogPostToBlogPostDTOMapper = blogPostToBlogPostDTOMapper;
    }

    @Override
    public BlogPostDTO save(CreateBlogPostDTO createBlogPostDTO) {
        BlogPost blogPost = createBlogPostDTOToBlogPostMapper.map(createBlogPostDTO);
        if (createBlogPostDTO.getTags().size() != 0) {
            blogPost.setTags(
                    createBlogPostDTO.getTags()
                            .stream()
                            .map(tagName -> tagRepository.findByName(tagName)
                                    .orElseGet(() -> tagRepository.save(new Tag(tagName))))
                            .collect(Collectors.toList())
            );
        }
        blogPost = blogPostRepository.save(blogPost);
        return blogPostToBlogPostDTOMapper.map(blogPost);
    }

    @Override
    public BlogPostDTO update(Long id, UpdateBlogPostDTO updateBlogPostDTO) {
        BlogPost blogPost = findBlogPostById(id);
        Date now = Date.from(timeStampProvider.now().toInstant(ZoneOffset.UTC));
        String plainText = blogPostContentValidationService
                .validateAndGetPlainText(updateBlogPostDTO.getContent());
        blogPost.setPlainText(plainText);
        blogPost.setLastUpdateDate(now);
        blogPost.setTags(updateBlogPostDTO.getTags()
                .stream()
                .map(tagName -> tagRepository.findByName(tagName)
                        .orElseGet(() -> tagRepository.save(new Tag(tagName))))
                .collect(Collectors.toList()));
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
        Date now = Date.from(timeStampProvider.now().toInstant(ZoneOffset.UTC));
        blogPost.setDeleted(true);
        blogPost.setDeletedAt(now);
        blogPost.setLastUpdateDate(now);
        blogPost.setDeletedBy(authenticationFacade.getCurrentUser());
        blogPostRepository.save(blogPost);
    }

    @Override
    public BlogPostDTO restore(Long id) {
        BlogPost blogPost = findDeletedBlogPostById(id);
        Date now = Date.from(timeStampProvider.now().toInstant(ZoneOffset.UTC));
        blogPost.setDeleted(false);
        blogPost.setDeletedAt(null);
        blogPost.setDeletedBy(null);
        blogPost.setLastUpdateDate(now);
        blogPost = blogPostRepository.save(blogPost);
        return blogPostToBlogPostDTOMapper.map(blogPost);
    }

    private BlogPost findBlogPostById(Long id) {
        BlogPost blogPost = blogPostRepository.findById(id)
                .orElseThrow(() -> new BlogPostNotFoundException("Blog post with given id "
                        + id + " could not be found."));
        if (blogPost.isDeleted()) {
            throw  new BlogPostNotFoundException("Blog post with given id "
                    + id + " could not be found.");
        }
        return blogPost;
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

    @Override
    public UserDTO findAuthorOfBlogPost(Long blogPostId) {
        return userToUserDTOMapper.map(findBlogPostById(blogPostId).getAuthor());
    }

    @ValidatePaginationParameters
    @Override
    public List<BlogPostDTO> getFeed(@Page int page, @PageSize(max = 50) int pageSize) {
        PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.Direction.DESC, "id");
        User currentUser = authenticationFacade.getCurrentUser();

        List<Notification> notifications = notificationRepository
                .findByRecipientAndNotificationType(currentUser, NotificationType.NEW_BLOG_POST, pageRequest);
        List<Long> blogPostIds = notifications.stream()
                .map(Notification::getNotificationGeneratorId)
                .collect(Collectors.toList());

        return blogPostRepository.findAllById(blogPostIds)
                .stream()
                .filter(blogPost -> !blogPost.isDeleted())
                .map(blogPostToBlogPostDTOMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    @ValidatePaginationParameters
    public List<BlogPostDTO> getMostPopularForWeek(@Page int page,
                                                   @PageSize(max = 50) int pageSize) {
        LocalDateTime now = timeStampProvider.now();
        LocalDateTime weekAgo = timeStampProvider.weekAgo();
        return findMostPopularInPeriod(weekAgo, now, page, pageSize)
                .stream()
                .map(blogPostToBlogPostDTOMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    @ValidatePaginationParameters
    public List<BlogPostDTO> getMostPopularForMonth(@Page int page,
                                                    @PageSize(max = 50) int pageSize) {
        LocalDateTime now = timeStampProvider.now();
        LocalDateTime monthAgo = timeStampProvider.monthAgo();
        return findMostPopularInPeriod(monthAgo, now, page, pageSize)
                .stream()
                .map(blogPostToBlogPostDTOMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    @ValidatePaginationParameters
    public List<BlogPostDTO> getMostPopularForYear(@Page int page,
                                                   @PageSize(max = 50) int pageSize) {
        LocalDateTime now = timeStampProvider.now();
        LocalDateTime yearAgo = timeStampProvider.yearAgo();
        return findMostPopularInPeriod(yearAgo, now, page, pageSize)
                .stream()
                .map(blogPostToBlogPostDTOMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    @ValidatePaginationParameters
    public List<BlogPostDTO> getMostPopularForPeriod(Date from,
                                                     Date to,
                                                     @Page int page,
                                                     @PageSize(max = 50) int pageSize) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        return blogPostRepository.findMostPopularForPeriod(from, to, pageRequest)
                .stream()
                .map(blogPostToBlogPostDTOMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<BlogPostDTO> findPinnedByBlog(Long blogId) {
        Blog blog = findBlogById(blogId);
        return blogPostRepository.findByBlogAndPinnedAndDeletedFalseOrderByPinDateDesc(blog, true)
                .stream()
                .map(blogPostToBlogPostDTOMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public BlogPostDTO pin(Long blogPostId) {
        BlogPost blogPost = findBlogPostById(blogPostId);
        List<BlogPost> pinnedBlogPosts = blogPostRepository
                .findByBlogAndPinnedAndDeletedFalseOrderByPinDateDesc(blogPost.getBlog(), true);
        if (pinnedBlogPosts.size() >= 5) {
            throw new PinnedBlogPostsLimitHasBeenReachedException("Pinned blog posts limit " +
                    "has been reached. You can pin up to 5 blog posts.");
        }
        blogPost.setPinned(true);
        Date now = Date.from(timeStampProvider.now().toInstant(ZoneOffset.UTC));
        blogPost.setPinDate(now);
        blogPost.setLastUpdateDate(now);
        return blogPostToBlogPostDTOMapper.map(blogPostRepository.save(blogPost));
    }

    @Override
    public BlogPostDTO unpin(Long blogPostId) {
        BlogPost blogPost = findBlogPostById(blogPostId);
        blogPost.setPinned(false);
        blogPost.setPinDate(null);
        blogPost.setLastUpdateDate(Date.from(timeStampProvider.now().toInstant(ZoneOffset.UTC)));
        return blogPostToBlogPostDTOMapper.map(blogPostRepository.save(blogPost));
    }

    @Override
    @ValidatePaginationParameters
    @ValidateCollectionSize
    public List<BlogPostDTO> search(
            String query,
            @CollectionArgument(maxSize = 10) List<String> tagNames,
            @Page int page,
            @PageSize(max = 50) int pageSize,
            @SortingDirection String sortingDirection,
            @SortBy(allowed = {"id", "createdAt", "popularity"}) String sortBy) {
        List<Tag> tags = tagNames.isEmpty()
                ? Collections.emptyList()
                : tagRepository.findAllByName(tagNames);
        List<BlogPost> blogPosts;
        PageRequest pageRequest;
        Sort.Direction direction = SortingDirectionUtils.convertFromString(sortingDirection);
        if ("popularity".equals(sortBy)) {
            pageRequest = PageRequest.of(page, pageSize);
            System.out.println(tags.isEmpty());
            if (direction.equals(Sort.Direction.ASC)) {
                blogPosts = tags.isEmpty()
                        ? blogPostRepository.searchSortByPopularityAsc(query, pageRequest)
                        : blogPostRepository.searchSortByPopularityAsc(query, tags, pageRequest);
            } else {
                blogPosts = tags.isEmpty()
                        ? blogPostRepository.searchSortByPopularityDesc(query, pageRequest)
                        : blogPostRepository.searchSortByPopularityDesc(query, tags, pageRequest);
            }
        } else {
            pageRequest = PageRequest.of(page, pageSize, direction, sortBy);
            blogPosts = tags.isEmpty()
                    ? blogPostRepository.search(query, pageRequest)
                    : blogPostRepository.search(query, tags, pageRequest);
        }

        return blogPosts.stream()
                .map(blogPostToBlogPostDTOMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    @ValidateCollectionSize
    public void deleteMultiple(@CollectionArgument(maxSize = 30) List<Long> ids) {
        List<BlogPost> blogPosts = blogPostRepository.findAllById(ids);
        blogPosts.forEach(blogPost -> {
            Date now = Date.from(timeStampProvider.now().toInstant(ZoneOffset.UTC));
            blogPost.setDeleted(true);
            blogPost.setDeletedAt(Date.from(timeStampProvider.now().toInstant(ZoneOffset.UTC)));
            blogPost.setLastUpdateDate(now);
            blogPost.setDeletedBy(authenticationFacade.getCurrentUser());
        });
        blogPostRepository.saveAll(blogPosts);
    }

    private List<BlogPost> findMostPopularInPeriod(LocalDateTime from, LocalDateTime to, int page, int pageSize) {
        Date fromDate = Date.from(from.toInstant(ZoneOffset.UTC));
        Date toDate = Date.from(to.toInstant(ZoneOffset.UTC));
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        return blogPostRepository.findMostPopularForPeriod(fromDate, toDate, pageRequest);
    }

    private Blog findBlogById(Long blogId) {
        return blogRepository.findById(blogId)
                .orElseThrow(() -> new BlogNotFoundException("Blog with given id "
                        + blogId + " could not be found."));
    }
}