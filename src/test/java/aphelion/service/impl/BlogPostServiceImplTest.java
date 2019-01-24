package aphelion.service.impl;

import aphelion.exception.BlogNotFoundException;
import aphelion.exception.BlogPostNotFoundException;
import aphelion.exception.PinnedBlogPostsLimitHasBeenReachedException;
import aphelion.mapper.BlogPostToBlogPostDTOMapper;
import aphelion.mapper.BlogPostToBlogPostMinifiedDTOMapper;
import aphelion.mapper.CreateBlogPostDTOToBlogPostMapper;
import aphelion.mapper.UserToUserDTOMapper;
import aphelion.model.domain.Blog;
import aphelion.model.domain.BlogPost;
import aphelion.model.domain.BlogPostPublisherType;
import aphelion.model.domain.Notification;
import aphelion.model.domain.NotificationType;
import aphelion.model.domain.User;
import aphelion.model.dto.BlogMinifiedDTO;
import aphelion.model.dto.BlogPostDTO;
import aphelion.model.dto.BlogPostMinifiedDTO;
import aphelion.model.dto.BlogPostPublisher;
import aphelion.model.dto.CreateBlogPostDTO;
import aphelion.model.dto.UpdateBlogPostDTO;
import aphelion.model.dto.UserDTO;
import aphelion.repository.BlogPostRepository;
import aphelion.repository.BlogRepository;
import aphelion.repository.NotificationRepository;
import aphelion.repository.TagRepository;
import aphelion.security.AuthenticationFacade;
import aphelion.service.BlogPostContentValidationService;
import aphelion.service.TimeStampProvider;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class BlogPostServiceImplTest {

    @Mock
    private BlogPostRepository mockBlogPostRepository;
    @Mock
    private BlogRepository mockBlogRepository;
    @Mock
    private TagRepository mockTagRepository;
    @Mock
    private NotificationRepository mockNotificationRepository;
    @Mock
    private AuthenticationFacade mockAuthenticationFacade;
    @Mock
    private BlogPostToBlogPostMinifiedDTOMapper mockBlogPostToBlogPostMinifiedDTOMapper;
    @Mock
    private CreateBlogPostDTOToBlogPostMapper mockCreateBlogPostDTOToBlogPostMapper;
    @Mock
    private UserToUserDTOMapper mockUserToUserDTOMapper;
    @Mock
    private BlogPostContentValidationService mockBlogPostContentValidationService;
    @Mock
    private BlogPostToBlogPostDTOMapper mockBlogPostToBlogPostDTOMapper;
    @Mock
    private TimeStampProvider mockTimeStampProvider;

    private BlogPostServiceImpl blogPostServiceImplUnderTest;

    @Before
    public void setUp() {
        initMocks(this);
        blogPostServiceImplUnderTest = new BlogPostServiceImpl(mockBlogPostRepository, mockBlogRepository, mockTagRepository, mockNotificationRepository, mockAuthenticationFacade, mockBlogPostToBlogPostMinifiedDTOMapper, mockCreateBlogPostDTOToBlogPostMapper, mockUserToUserDTOMapper, mockBlogPostContentValidationService, mockTimeStampProvider);
        blogPostServiceImplUnderTest.setBlogPostToBlogPostDTOMapper(mockBlogPostToBlogPostDTOMapper);
    }

    @Test
    public void testSave() {
        // Setup
        Date now = Date.from(Instant.now());
        final CreateBlogPostDTO createBlogPostDTO = CreateBlogPostDTO.builder()
                .blogId(1L)
                .content(ImmutableMap.of("content", "test"))
                .publishedBy(BlogPostPublisherType.BLOG)
                .title("Title")
                .build();
        final BlogPost blogPost = BlogPost.builder()
                .blog(Blog.builder().id(1L).build())
                .author(User.builder().id(1L).build())
                .publishedBy(BlogPostPublisherType.BLOG)
                .createdAt(now)
                .deleted(false)
                .build();
        when(mockCreateBlogPostDTOToBlogPostMapper.map(createBlogPostDTO)).thenReturn(blogPost);
        when(mockBlogPostRepository.save(blogPost)).thenReturn(blogPost);
        final BlogPostDTO expectedResult = BlogPostDTO
                .builder()
                .blogId(1L)
                .content(ImmutableMap.of("content", "test"))
                .publisher(BlogPostPublisher.builder().id(1L).build())
                .canBeDeleted(true)
                .canBeEdited(true)
                .createdAt(now)
                .deleted(false)
                .likedByCurrentUser(false)
                .build();
        when(mockBlogPostToBlogPostDTOMapper.map(blogPost)).thenReturn(expectedResult);

        // Run the test
        final BlogPostDTO result = blogPostServiceImplUnderTest.save(createBlogPostDTO);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    public void testUpdate() {
        // Setup
        final Long id = 1L;
        final UpdateBlogPostDTO updateBlogPostDTO = UpdateBlogPostDTO.builder()
                .content(ImmutableMap.of("content", "test2"))
                .tags(new ArrayList<>())
                .title("Title")
                .build();
        final BlogPostDTO expectedResult = BlogPostDTO.builder()
                .id(id)
                .canBeDeleted(true)
                .canBeEdited(true)
                .content(ImmutableMap.of("content", "test2"))
                .blogId(1L)
                .deleted(false)
                .build();
        final BlogPost blogPost = BlogPost.builder()
                .id(id)
                .blog(Blog.builder().id(1L).build())
                .content(ImmutableMap.of("content", "test"))
                .build();
        when(mockBlogPostRepository.findById(id)).thenReturn(Optional.of(blogPost));
        when(mockBlogPostRepository.save(blogPost)).thenReturn(blogPost);
        when(mockBlogPostToBlogPostDTOMapper.map(blogPost)).thenReturn(expectedResult);

        // Run the test
        final BlogPostDTO result = blogPostServiceImplUnderTest.update(id, updateBlogPostDTO);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test(expected = BlogPostNotFoundException.class)
    public void testUpdate_whenBlogPostNotFound_thenExceptionIsThrown() {
        final Long id = 2L;
        UpdateBlogPostDTO updateBlogPostDTO = UpdateBlogPostDTO.builder()
                .content(ImmutableMap.of("content", "test"))
                .build();
        when(mockBlogPostRepository.findById(id)).thenReturn(Optional.empty());

        blogPostServiceImplUnderTest.update(id, updateBlogPostDTO);
    }

    @Test
    public void testFindById() {
        // Setup
        final Long id = 1L;
        final BlogPostDTO expectedResult = BlogPostDTO.builder()
                .id(id)
                .deleted(false)
                .blogId(1L)
                .content(ImmutableMap.of("content", "test"))
                .canBeDeleted(false)
                .canBeDeleted(false)
                .title("Title")
                .build();
        final BlogPost blogPost = BlogPost.builder()
                .id(id)
                .deleted(false)
                .blog(Blog.builder().id(1L).build())
                .content(ImmutableMap.of("content", "test"))
                .title("title")
                .build();
        when(mockBlogPostRepository.findById(id)).thenReturn(Optional.of(blogPost));
        when(mockBlogPostToBlogPostDTOMapper.map(blogPost)).thenReturn(expectedResult);

        // Run the test
        final BlogPostDTO result = blogPostServiceImplUnderTest.findById(id);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test(expected = BlogPostNotFoundException.class)
    public void testFindById_whenBlogPostNotFound_thenExceptionIsThrown() {
        final Long id = 2L;
        when(mockBlogPostRepository.findById(id)).thenReturn(Optional.empty());

        blogPostServiceImplUnderTest.findById(id);
    }

    @Test
    public void testFindDeletedById() {
        // Setup
        final Long id = 3L;
        final Date createdAt = new GregorianCalendar(2017, 12, 17).getTime();
        final Date deletedAt = new GregorianCalendar(2018, 10, 12).getTime();
        final BlogPost blogPost = BlogPost.builder()
                .id(id)
                .title("Title")
                .content(ImmutableMap.of("content", "test"))
                .blog(Blog.builder().id(1).build())
                .deleted(true)
                .createdAt(createdAt)
                .deletedAt(deletedAt)
                .build();
        final BlogPostDTO expectedResult = BlogPostDTO.builder()
                .id(id)
                .deleted(true)
                .createdAt(createdAt)
                .deleted(true)
                .blogId(1L)
                .title("Title")
                .content(ImmutableMap.of("content", "test"))
                .canBeEdited(true)
                .canBeDeleted(true)
                .deletedByUserId(2L)
                .build();
        when(mockBlogPostRepository.findByIdAndDeleted(id, true)).thenReturn(Optional.of(blogPost));
        when(mockBlogPostToBlogPostDTOMapper.map(blogPost)).thenReturn(expectedResult);
        // Run the test
        final BlogPostDTO result = blogPostServiceImplUnderTest.findDeletedById(id);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFindDeletedByCurrentUserInBlog() {
        // Setup
        final Long blogId = 1L;
        final int page = 0;
        final int pageSize = 5;
        final String sortingDirection = "asc";
        final String sortBy = "id";
        final List<BlogPostMinifiedDTO> expectedResult = Arrays.asList(
                BlogPostMinifiedDTO.builder()
                        .id(1L)
                        .title("title")
                        .blog(BlogMinifiedDTO.builder().id(blogId).build())
                        .content(ImmutableMap.of("content", "test"))
                        .build(),
                BlogPostMinifiedDTO.builder()
                        .id(2L)
                        .title("title")
                        .blog(BlogMinifiedDTO.builder().id(blogId).build())
                        .content(ImmutableMap.of("content", "test2"))
                        .build()
        );
        final User currentUser = User.builder().id(1L).build();
        final Blog blog = Blog.builder().id(blogId).build();
        final List<BlogPost> blogPosts = Arrays.asList(
                BlogPost.builder()
                        .id(1L)
                        .blog(blog)
                        .author(currentUser)
                        .content(ImmutableMap.of("content", "test"))
                        .title("title")
                        .build(),
                BlogPost.builder()
                        .id(2L)
                        .blog(blog)
                        .author(currentUser)
                        .content(ImmutableMap.of("content", "test2"))
                        .title("title")
                        .build()
        );
        final PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.Direction.ASC, sortBy);
        when(mockAuthenticationFacade.getCurrentUser()).thenReturn(currentUser);
        when(mockBlogRepository.findById(blogId)).thenReturn(Optional.of(blog));
        when(mockBlogPostRepository.findByBlogAndDeletedAndDeletedBy(blog, true, currentUser, pageRequest))
                .thenReturn(blogPosts);
        for (int index = 0; index < blogPosts.size(); index++) {
            when(mockBlogPostToBlogPostMinifiedDTOMapper.map(blogPosts.get(index)))
                    .thenReturn(expectedResult.get(index));
        }

        // Run the test
        final List<BlogPostMinifiedDTO> result = blogPostServiceImplUnderTest.findDeletedByCurrentUserInBlog(blogId, page, pageSize, sortingDirection, sortBy);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    public void testDelete() {
        // Setup
        final Long id = 1L;
        final BlogPost blogPost = BlogPost.builder().id(id).build();
        final User currentUser = User.builder().id(1L).build();
        when(mockBlogPostRepository.findById(id)).thenReturn(Optional.of(blogPost));
        when(mockAuthenticationFacade.getCurrentUser()).thenReturn(currentUser);

        // Run the test
        blogPostServiceImplUnderTest.delete(id);

        // Verify the results
        verify(mockBlogPostRepository, times(1)).save(blogPost);
        assertTrue(blogPost.isDeleted());
        assertEquals(blogPost.getDeletedBy(), currentUser);
        assertNotNull(blogPost.getDeletedAt());
    }

    @Test(expected = BlogPostNotFoundException.class)
    public void testDelete_whenBlogPostNotFound_thenExceptionIsThrown() {
        final Long id = 2L;
        when(mockBlogPostRepository.findById(id)).thenReturn(Optional.empty());

        blogPostServiceImplUnderTest.delete(id);
    }

    @Test
    public void testRestore() {
        // Setup
        final Long id = 1L;
        final BlogPostDTO expectedResult = BlogPostDTO.builder()
                .id(1L)
                .content(ImmutableMap.of("content", "test"))
                .canBeDeleted(true)
                .canBeEdited(true)
                .blogId(2L)
                .build();
        final BlogPost blogPost = BlogPost.builder()
                .id(id)
                .content(ImmutableMap.of("content", "test"))
                .deleted(true)
                .deletedAt(new GregorianCalendar(2018, 10, 15, 18, 30, 1).getTime())
                .deletedBy(User.builder().id(1L).build())
                .title("title")
                .build();
        when(mockBlogPostRepository.findByIdAndDeleted(id, true)).thenReturn(Optional.of(blogPost));
        when(mockBlogPostRepository.save(blogPost)).thenReturn(blogPost);
        when(mockBlogPostToBlogPostDTOMapper.map(blogPost)).thenReturn(expectedResult);

        // Run the test
        final BlogPostDTO result = blogPostServiceImplUnderTest.restore(id);

        // Verify the results
        assertEquals(expectedResult, result);
        assertFalse(blogPost.isDeleted());
        assertNull(blogPost.getDeletedAt());
        assertNull(blogPost.getDeletedBy());
    }

    @Test
    public void testFindByBlog() {
        // Setup
        final Long blogId = 1L;
        final int page = 0;
        final int pageSize = 5;
        final String sortingDirection = "asc";
        final String sortBy = "id";
        final List<BlogPostDTO> expectedResult = Arrays.asList(
                BlogPostDTO.builder()
                        .id(1L)
                        .content(ImmutableMap.of("content", "test"))
                        .title("title")
                        .canBeEdited(false)
                        .canBeDeleted(false)
                        .blogId(blogId)
                        .build(),
                BlogPostDTO.builder()
                        .id(2L)
                        .content(ImmutableMap.of("content", "test2"))
                        .title("title")
                        .canBeEdited(false)
                        .canBeDeleted(false)
                        .blogId(blogId)
                        .build()
        );
        final Blog blog = Blog.builder().id(blogId).build();
        final List<BlogPost> blogPosts = Arrays.asList(
                BlogPost.builder()
                        .id(1L)
                        .content(ImmutableMap.of("content", "test"))
                        .title("title")
                        .blog(blog)
                        .build(),
                BlogPost.builder()
                        .id(2L)
                        .content(ImmutableMap.of("content", "test2"))
                        .title("title")
                        .blog(blog)
                        .build()
        );
        final PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.Direction.ASC, sortBy);
        when(mockBlogRepository.findById(blogId)).thenReturn(Optional.of(blog));
        when(mockBlogPostRepository.findByBlog(blog, pageRequest)).thenReturn(blogPosts);
        when(mockBlogPostToBlogPostDTOMapper.map(blogPosts.get(0))).thenReturn(expectedResult.get(0));
        when(mockBlogPostToBlogPostDTOMapper.map(blogPosts.get(1))).thenReturn(expectedResult.get(1));

        // Run the test
        final List<BlogPostDTO> result = blogPostServiceImplUnderTest.findByBlog(blogId, page, pageSize, sortingDirection, sortBy);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test(expected = BlogNotFoundException.class)
    public void testFindByBlog_whenBlogNotFound_thenExceptionIsThrown() {
        final Long blogId = 2L;
        final int page = 0;
        final int pageSize = 10;
        final String sortingDirection = "asc";
        final String sortBy = "id";
        when(mockBlogRepository.findById(blogId)).thenReturn(Optional.empty());

        blogPostServiceImplUnderTest.findByBlog(blogId, page, pageSize, sortingDirection, sortBy);
    }

    @Test
    public void testFindAuthorOfBlogPost() {
        // Setup
        final Long blogPostId = 1L;
        final User user = User.builder()
                .id(2L)
                .displayedName("Username")
                .build();
        final BlogPost blogPost = BlogPost.builder()
                .id(1L)
                .author(user)
                .blog(Blog.builder().id(1L).build())
                .build();
        final UserDTO expectedResult = UserDTO.builder()
                .id(1L)
                .displayedName("Username")
                .build();
        when(mockBlogPostRepository.findById(1L)).thenReturn(Optional.of(blogPost));
        when(mockUserToUserDTOMapper.map(user)).thenReturn(expectedResult);

        // Run the test
        final UserDTO result = blogPostServiceImplUnderTest.findAuthorOfBlogPost(blogPostId);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetFeed() {
        // Setup
        final int page = 0;
        final int pageSize = 5;
        final PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.Direction.DESC, "id");
        final User currentUser = User.builder().id(1L).build();
        final List<BlogPost> blogPosts = Arrays.asList(
                BlogPost.builder().id(1L).build(),
                BlogPost.builder().id(2L).build()
        );
        List<Notification> notifications = Arrays.asList(
                Notification.builder()
                        .id(1L)
                        .notificationGeneratorId(1L)
                        .notificationType(NotificationType.NEW_BLOG_POST)
                        .build(),
                Notification.builder()
                        .id(2L)
                        .notificationGeneratorId(2L)
                        .notificationType(NotificationType.NEW_BLOG_POST)
                        .build()
        );
        final List<BlogPostDTO> expectedResult = Arrays.asList(
                BlogPostDTO.builder().id(1L).build(),
                BlogPostDTO.builder().id(2L).build()
        );
        when(mockAuthenticationFacade.getCurrentUser()).thenReturn(currentUser);
        when(mockNotificationRepository.findByRecipientAndNotificationType(
                currentUser, NotificationType.NEW_BLOG_POST, pageRequest
        )).thenReturn(notifications);
        when(mockBlogPostRepository.findAllById(
                notifications
                        .stream()
                        .map(Notification::getNotificationGeneratorId)
                        .collect(Collectors.toList())
        )).thenReturn(blogPosts);
        when(mockBlogPostToBlogPostDTOMapper.map(blogPosts.get(0))).thenReturn(expectedResult.get(0));
        when(mockBlogPostToBlogPostDTOMapper.map(blogPosts.get(1))).thenReturn(expectedResult.get(1));

        // Run the test
        final List<BlogPostDTO> result = blogPostServiceImplUnderTest.getFeed(page, pageSize);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetMostPopularForWeek() {
        // Setup
        final int page = 0;
        final int pageSize = 5;
        final List<BlogPostDTO> expectedResult = Arrays.asList(
                BlogPostDTO.builder().id(1L).build(),
                BlogPostDTO.builder().id(2L).build()
        );
        final List<BlogPost> blogPosts = Arrays.asList(
                BlogPost.builder().id(1L).build(),
                BlogPost.builder().id(2L).build()
        );
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime weekAgo = now.minusWeeks(1);
        final PageRequest pageRequest = PageRequest.of(page, pageSize);
        when(mockTimeStampProvider.now()).thenReturn(now);
        when(mockTimeStampProvider.weekAgo()).thenReturn(weekAgo);
        when(mockBlogPostRepository
                .findMostPopularForPeriod(
                        Date.from(weekAgo.toInstant(ZoneOffset.UTC)),
                        Date.from(now.toInstant(ZoneOffset.UTC)),
                        pageRequest
                )
        ).thenReturn(blogPosts);
        for (int index = 0; index < blogPosts.size(); index++) {
            when(mockBlogPostToBlogPostDTOMapper.map(blogPosts.get(index))).thenReturn(expectedResult.get(index));
        }

        // Run the test
        final List<BlogPostDTO> result = blogPostServiceImplUnderTest.getMostPopularForWeek(page, pageSize);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetMostPopularForMonth() {
        // Setup
        final int page = 0;
        final int pageSize = 5;
        final List<BlogPostDTO> expectedResult = Arrays.asList(
                BlogPostDTO.builder().id(1L).build(),
                BlogPostDTO.builder().id(2L).build()
        );
        final List<BlogPost> blogPosts = Arrays.asList(
                BlogPost.builder().id(1L).build(),
                BlogPost.builder().id(2L).build()
        );
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime monthAgo = now.minusMonths(1);
        final PageRequest pageRequest = PageRequest.of(page, pageSize);
        when(mockTimeStampProvider.now()).thenReturn(now);
        when(mockTimeStampProvider.monthAgo()).thenReturn(monthAgo);
        when(mockBlogPostRepository
                .findMostPopularForPeriod(
                        Date.from(monthAgo.toInstant(ZoneOffset.UTC)),
                        Date.from(now.toInstant(ZoneOffset.UTC)),
                        pageRequest
                )
        ).thenReturn(blogPosts);
        for (int index = 0; index < blogPosts.size(); index++) {
            when(mockBlogPostToBlogPostDTOMapper.map(blogPosts.get(index))).thenReturn(expectedResult.get(index));
        }

        // Run the test
        final List<BlogPostDTO> result = blogPostServiceImplUnderTest.getMostPopularForMonth(page, pageSize);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetMostPopularForYear() {
        // Setup
        final int page = 0;
        final int pageSize = 5;
        final List<BlogPostDTO> expectedResult = Arrays.asList(
                BlogPostDTO.builder().id(1L).build(),
                BlogPostDTO.builder().id(2L).build()
        );
        final List<BlogPost> blogPosts = Arrays.asList(
                BlogPost.builder().id(1L).build(),
                BlogPost.builder().id(2L).build()
        );
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime yearAgo = now.minusYears(1);
        final PageRequest pageRequest = PageRequest.of(page, pageSize);
        when(mockTimeStampProvider.now()).thenReturn(now);
        when(mockTimeStampProvider.yearAgo()).thenReturn(yearAgo);
        when(mockBlogPostRepository
                .findMostPopularForPeriod(
                        Date.from(yearAgo.toInstant(ZoneOffset.UTC)),
                        Date.from(now.toInstant(ZoneOffset.UTC)),
                        pageRequest
                )
        ).thenReturn(blogPosts);
        for (int index = 0; index < blogPosts.size(); index++) {
            when(mockBlogPostToBlogPostDTOMapper.map(blogPosts.get(index))).thenReturn(expectedResult.get(index));
        }


        // Run the test
        final List<BlogPostDTO> result = blogPostServiceImplUnderTest.getMostPopularForYear(page, pageSize);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetMostPopularForPeriod() {
        // Setup
        final Date from = new GregorianCalendar(2017, 1, 1).getTime();
        final Date to = new GregorianCalendar(2018, 3, 10).getTime();
        final int page = 0;
        final int pageSize = 10;
        final List<BlogPostDTO> expectedResult = Arrays.asList(
                BlogPostDTO.builder().id(1L).build(),
                BlogPostDTO.builder().id(2L).build()
        );
        final List<BlogPost> blogPosts = Arrays.asList(
                BlogPost.builder().id(1L).build(),
                BlogPost.builder().id(2L).build()
        );
        final PageRequest pageRequest = PageRequest.of(page, pageSize);
        when(mockBlogPostRepository.findMostPopularForPeriod(from, to, pageRequest)).thenReturn(blogPosts);
        for (int index = 0; index < blogPosts.size(); index++) {
            when(mockBlogPostToBlogPostDTOMapper.map(blogPosts.get(index))).thenReturn(expectedResult.get(index));
        }

        // Run the test
        final List<BlogPostDTO> result = blogPostServiceImplUnderTest.getMostPopularForPeriod(from, to, page, pageSize);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    public void testPin() {
        // Setup
        final LocalDateTime nowTime = LocalDateTime.now();
        final Date now = Date.from(nowTime.toInstant(ZoneOffset.UTC));
        final Long blogPostId = 1L;
        final BlogPost blogPost = BlogPost.builder()
                .id(blogPostId)
                .pinned(false)
                .pinDate(null)
                .build();
        final BlogPostDTO expectedResult = BlogPostDTO.builder()
                .id(blogPostId)
                .pinned(true)
                .pinDate(now)
                .build();
        when(mockBlogPostRepository.findById(blogPostId)).thenReturn(Optional.of(blogPost));
        when(mockBlogPostRepository.save(blogPost)).thenReturn(blogPost);
        when(mockTimeStampProvider.now()).thenReturn(nowTime);
        when(mockBlogPostToBlogPostDTOMapper.map(blogPost)).thenReturn(expectedResult);

        // Run the test
        final BlogPostDTO result = blogPostServiceImplUnderTest.pin(blogPostId);

        // Verify the results
        assertEquals(expectedResult, result);
        assertTrue(blogPost.isPinned());
        assertEquals(blogPost.getPinDate(), now);
    }

    @Test(expected = PinnedBlogPostsLimitHasBeenReachedException.class)
    public void testPin_whenPinnedBlogPostsLimitReached_thenExceptionIsThrown() {
        // Setup
        final Long blogPostId = 1L;
        final Blog blog = Blog.builder().id(1L).build();
        final BlogPost blogPost = BlogPost.builder().id(blogPostId).blog(blog).build();
        final List<BlogPost> blogPosts = Arrays.asList(
                BlogPost.builder().id(2L).build(),
                BlogPost.builder().id(3L).build(),
                BlogPost.builder().id(4L).build(),
                BlogPost.builder().id(5L).build(),
                BlogPost.builder().id(6L).build()
        );
        when(mockBlogPostRepository.findById(blogPostId)).thenReturn(Optional.of(blogPost));
        when(mockBlogPostRepository.findByBlogAndPinnedOrderByPinDateDesc(blog, true))
                .thenReturn(blogPosts);

        // Run the test
       blogPostServiceImplUnderTest.pin(blogPostId);
    }
}