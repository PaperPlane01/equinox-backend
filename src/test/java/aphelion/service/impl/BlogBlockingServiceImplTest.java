package aphelion.service.impl;

import aphelion.exception.BlogBlockingNotFoundException;
import aphelion.exception.BlogNotFoundException;
import aphelion.mapper.BlogBlockingToBlogBlockingDTOMapper;
import aphelion.mapper.CreateBlogBlockingDTOToBlogBlockingMapper;
import aphelion.model.domain.Blog;
import aphelion.model.domain.BlogBlocking;
import aphelion.model.domain.User;
import aphelion.model.dto.BlogBlockingDTO;
import aphelion.model.dto.BlogMinifiedDTO;
import aphelion.model.dto.CreateBlogBlockingDTO;
import aphelion.model.dto.UpdateBlogBlockingDTO;
import aphelion.model.dto.UserDTO;
import aphelion.repository.BlogBlockingRepository;
import aphelion.repository.BlogRepository;
import aphelion.repository.UserRepository;
import aphelion.service.TimeStampProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class BlogBlockingServiceImplTest {

    @Mock
    private BlogBlockingRepository mockBlogBlockingRepository;
    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private BlogRepository mockBlogRepository;
    @Mock
    private BlogBlockingToBlogBlockingDTOMapper mockBlogBlockingToBlogBlockingDTOMapper;
    @Mock
    private CreateBlogBlockingDTOToBlogBlockingMapper mockCreateBlogBlockingDTOToBlogBlockingMapper;
    @Mock
    private TimeStampProvider mockTimeStampProvider;

    private BlogBlockingServiceImpl blogBlockingServiceImplUnderTest;

    @Before
    public void setUp() {
        initMocks(this);
        blogBlockingServiceImplUnderTest = new BlogBlockingServiceImpl(mockBlogBlockingRepository, mockUserRepository, mockBlogRepository, mockBlogBlockingToBlogBlockingDTOMapper, mockCreateBlogBlockingDTOToBlogBlockingMapper, mockTimeStampProvider);
    }

    @Test
    public void testFindById() {
        // Setup
        final Long id = 1L;
        final Date startDate = new GregorianCalendar(2019, 1, 1, 10, 55, 56).getTime();
        final Date endDate = new GregorianCalendar(2019, 2, 15, 10, 12, 0).getTime();
        final BlogBlockingDTO expectedResult = BlogBlockingDTO.builder()
                .id(id)
                .blog(BlogMinifiedDTO.builder().id(1L).build())
                .blockedBy(UserDTO.builder().id(1L).build())
                .blockedUser(UserDTO.builder().id(2L).build())
                .startDate(startDate)
                .endDate(endDate)
                .build();
        final BlogBlocking blogBlocking = BlogBlocking.builder()
                .id(id)
                .blog(Blog.builder().id(1L).build())
                .blockedBy(User.builder().id(1L).build())
                .blockedUser(User.builder().id(2L).build())
                .startDate(startDate)
                .endDate(endDate)
                .build();
        when(mockBlogBlockingRepository.findById(id)).thenReturn(Optional.of(blogBlocking));
        when(mockBlogBlockingToBlogBlockingDTOMapper.map(blogBlocking)).thenReturn(expectedResult);

        // Run the test
        final BlogBlockingDTO result = blogBlockingServiceImplUnderTest.findById(id);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test(expected = BlogBlockingNotFoundException.class)
    public void testFindById_whenBlogBlockingNotFound_thenExceptionIsThrown() {
        //setup
        Long id = 2L;
        when(mockBlogBlockingRepository.findById(id)).thenReturn(Optional.empty());

        // Run the test
        blogBlockingServiceImplUnderTest.findById(id);
    }

    @Test
    public void testSave() {
        // Setup
        final Date now = Date.from(Instant.now());
        final CreateBlogBlockingDTO createBlogBlockingDTO = CreateBlogBlockingDTO
                .builder()
                .blogId(1L)
                .blockedUserId(2L)
                .endDate(new GregorianCalendar(2019, 12, 10, 15, 30, 0).getTime())
                .build();
        final BlogBlockingDTO expectedResult = BlogBlockingDTO.builder()
                .id(2L)
                .blog(BlogMinifiedDTO.builder().id(1L).build())
                .blockedBy(UserDTO.builder().id(1L).build())
                .blockedUser(UserDTO.builder().id(2L).build())
                .startDate(now)
                .endDate(new GregorianCalendar(2019, 12, 10, 15, 30, 0).getTime())
                .build();
        final BlogBlocking blogBlocking = BlogBlocking.builder()
                .id(2L)
                .blog(Blog.builder().id(1L).build())
                .blockedBy(User.builder().id(1L).build())
                .blockedUser(User.builder().id(2L).build())
                .startDate(now)
                .build();
        when(mockCreateBlogBlockingDTOToBlogBlockingMapper.map(createBlogBlockingDTO)).thenReturn(blogBlocking);
        when(mockBlogBlockingRepository.save(blogBlocking)).thenReturn(blogBlocking);
        when(mockBlogBlockingToBlogBlockingDTOMapper.map(blogBlocking)).thenReturn(expectedResult);

        // Run the test
        final BlogBlockingDTO result = blogBlockingServiceImplUnderTest.save(createBlogBlockingDTO);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    public void testUpdate() {
        // Setup
        final Long id = 1L;
        final Date endDate = new GregorianCalendar(2020, 1, 1, 10, 0, 0).getTime();
        final String reason = "new reason";
        final UpdateBlogBlockingDTO updateBlogBlockingDTO = UpdateBlogBlockingDTO.builder()
                .reason(reason)
                .endDate(new GregorianCalendar(2020, 1, 1, 10, 0, 0).getTime())
                .build();
        final BlogBlocking blogBlocking = BlogBlocking.builder()
                .id(id)
                .startDate(new GregorianCalendar(2018, 31, 12, 10, 0, 0).getTime())
                .endDate(new GregorianCalendar(2019, 5, 3, 14, 10, 10).getTime())
                .blockedUser(User.builder().id(1L).build())
                .blockedBy(User.builder().id(2L).build())
                .blog(Blog.builder().id(1L).build())
                .reason("old reason")
                .build();
        final BlogBlockingDTO expectedResult = BlogBlockingDTO.builder()
                .id(id)
                .startDate(new GregorianCalendar(2018, 31, 12, 10, 0, 0).getTime())
                .endDate(endDate)
                .blockedUser(UserDTO.builder().id(1L).build())
                .blockedBy(UserDTO.builder().id(2L).build())
                .blog(BlogMinifiedDTO.builder().id(1L).build())
                .build();
        when(mockBlogBlockingRepository.findById(id)).thenReturn(Optional.of(blogBlocking));
        when(mockBlogBlockingRepository.save(blogBlocking)).thenReturn(blogBlocking);
        when(mockBlogBlockingToBlogBlockingDTOMapper.map(blogBlocking)).thenReturn(expectedResult);

        // Run the test
        final BlogBlockingDTO result = blogBlockingServiceImplUnderTest.update(id, updateBlogBlockingDTO);

        // Verify the results
        assertEquals(expectedResult, result);
        assertEquals(blogBlocking.getEndDate(), endDate);
        assertEquals(blogBlocking.getReason(), reason);
    }

    @Test
    public void testFindNotEndedByBlockedUser() {
        // Setup
        final Long userId = 1L;
        final User user = User.builder().id(userId).build();
        final List<BlogBlockingDTO> expectedResult = Collections.singletonList(
                BlogBlockingDTO.builder().id(1L).build()
        );
        final List<BlogBlocking> blogBlockings = Collections.singletonList(
                BlogBlocking.builder().id(1L).build()
        );
        final LocalDateTime now = LocalDateTime.now();
        when(mockUserRepository.findById(userId)).thenReturn(Optional.of(user));
        when(mockTimeStampProvider.now()).thenReturn(now);
        when(mockBlogBlockingRepository
                .findAllByBlockedUserAndEndDateGreaterThan(user, Date.from(now.toInstant(ZoneOffset.UTC)))
        ).thenReturn(blogBlockings);
        for (int index = 0; index < blogBlockings.size(); index++) {
            when(mockBlogBlockingToBlogBlockingDTOMapper.map(blogBlockings.get(index)))
                    .thenReturn(expectedResult.get(index));
        }

        // Run the test
        final List<BlogBlockingDTO> result = blogBlockingServiceImplUnderTest.findNotEndedByBlockedUser(userId);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    public void testDelete() {
        // Setup
        final Long id = 1L;
        final BlogBlocking blogBlocking = BlogBlocking.builder().id(1).build();
        when(mockBlogBlockingRepository.findById(id)).thenReturn(Optional.of(blogBlocking));

        // Run the test
        blogBlockingServiceImplUnderTest.delete(id);

        // Verify the results
        verify(mockBlogBlockingRepository, times(1)).delete(blogBlocking);
    }

    @Test
    public void testIsUserBlockedInBlog() {
        // Setup
        final Long userId = 1L;
        final Long blogId = 1L;
        final boolean expectedResult = true;
        final Blog blog = Blog.builder().id(blogId).build();
        final User user = User.builder().id(userId).build();
        final LocalDateTime now = LocalDateTime.now();
        final List<BlogBlocking> blogBlockings = Collections.singletonList(
                BlogBlocking.builder().id(1L).blockedUser(user).blog(blog).build()
        );
        when(mockUserRepository.findById(userId)).thenReturn(Optional.of(user));
        when(mockBlogRepository.findById(blogId)).thenReturn(Optional.of(blog));
        when(mockTimeStampProvider.now()).thenReturn(now);
        when(mockBlogBlockingRepository.findAllByBlockedUserAndBlogAndEndDateGreaterThan(
                user, blog, Date.from(now.toInstant(ZoneOffset.UTC)))
        ).thenReturn(blogBlockings);

        // Run the test
        final boolean result = blogBlockingServiceImplUnderTest.isUserBlockedInBlog(userId, blogId);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByBlog() {
        // Setup
        final Long blogId = 1L;
        final int page = 0;
        final int pageSize = 5;
        final List<BlogBlockingDTO> expectedResult = Arrays.asList(
                BlogBlockingDTO.builder().id(1L).build(),
                BlogBlockingDTO.builder().id(2L).build(),
                BlogBlockingDTO.builder().id(3L).build(),
                BlogBlockingDTO.builder().id(4L).build(),
                BlogBlockingDTO.builder().id(5L).build()
        );
        final Blog blog = Blog.builder().id(blogId).build();
        final List<BlogBlocking> blogBlockings = Arrays.asList(
                BlogBlocking.builder().id(1L).build(),
                BlogBlocking.builder().id(2L).build(),
                BlogBlocking.builder().id(3L).build(),
                BlogBlocking.builder().id(4L).build(),
                BlogBlocking.builder().id(5L).build()
        );
        PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.Direction.DESC, "id");
        when(mockBlogRepository.findById(blogId)).thenReturn(Optional.of(blog));
        when(mockBlogBlockingRepository.findByBlog(blog, pageRequest)).thenReturn(blogBlockings);
        for (int index = 0; index < blogBlockings.size(); index++) {
            when(mockBlogBlockingToBlogBlockingDTOMapper.map(blogBlockings.get(index)))
                    .thenReturn(expectedResult.get(index));
        }

        // Run the test
        final List<BlogBlockingDTO> result = blogBlockingServiceImplUnderTest
                .findByBlog(blogId, page, pageSize);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test(expected = BlogNotFoundException.class)
    public void testFindByBlog_whenBlogNotFound_thenExceptionIsThrown() {
        // Setup
        final Long blogId = 2L;
        final int page = 0;
        final int pageSize = 5;
        when(mockBlogRepository.findById(blogId)).thenReturn(Optional.empty());

        // Run the test
        blogBlockingServiceImplUnderTest.findByBlog(blogId, page, pageSize);
    }

    @Test
    public void testFindNotEndedByBlog() {
        // Setup
        final Long blogId = 1L;
        final int page = 0;
        final int pageSize = 5;
        final List<BlogBlockingDTO> expectedResult = Arrays.asList(
                BlogBlockingDTO.builder().id(1L).build(),
                BlogBlockingDTO.builder().id(2L).build(),
                BlogBlockingDTO.builder().id(3L).build(),
                BlogBlockingDTO.builder().id(4L).build(),
                BlogBlockingDTO.builder().id(5L).build()
        );
        final List<BlogBlocking> blogBlockings = Arrays.asList(
                BlogBlocking.builder().id(1L).build(),
                BlogBlocking.builder().id(2L).build(),
                BlogBlocking.builder().id(3L).build(),
                BlogBlocking.builder().id(4L).build(),
                BlogBlocking.builder().id(5L).build()
        );
        final Blog blog = Blog.builder().id(blogId).build();
        final LocalDateTime now = LocalDateTime.now();
        final PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.Direction.DESC, "id");
        when(mockBlogRepository.findById(blogId)).thenReturn(Optional.of(blog));
        when(mockTimeStampProvider.now()).thenReturn(now);
        when(mockBlogBlockingRepository
                .findByBlogAndEndDateGreaterThan(blog, Date.from(now.toInstant(ZoneOffset.UTC)), pageRequest)
        ).thenReturn(blogBlockings);
        for (int index = 0; index < blogBlockings.size(); index++) {
            when(mockBlogBlockingToBlogBlockingDTOMapper.map(blogBlockings.get(index)))
                    .thenReturn(expectedResult.get(index));
        }

        // Run the test
        final List<BlogBlockingDTO> result = blogBlockingServiceImplUnderTest.findNotEndedByBlog(blogId, page, pageSize);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFindByBlogAndBlockedUserDisplayedUsernameContains() {
        // Setup
        final Long blogId = 1L;
        final String username = "username";
        final int page = 0;
        final int pageSize = 5;
        final List<BlogBlockingDTO> expectedResult = Arrays.asList(
                BlogBlockingDTO.builder().id(1L).build(),
                BlogBlockingDTO.builder().id(2L).build(),
                BlogBlockingDTO.builder().id(3L).build(),
                BlogBlockingDTO.builder().id(4L).build()
        );
        final List<BlogBlocking> blogBlockings = Arrays.asList(
                BlogBlocking.builder().id(1L).build(),
                BlogBlocking.builder().id(2L).build(),
                BlogBlocking.builder().id(3L).build(),
                BlogBlocking.builder().id(4L).build()
        );
        final Blog blog = Blog.builder().id(blogId).build();
        final PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.Direction.DESC, "id");
        when(mockBlogRepository.findById(blogId)).thenReturn(Optional.of(blog));
        when(mockBlogBlockingRepository.findByBlogAndBlockedUserDisplayedNameContains(blog, username,
                pageRequest)).thenReturn(blogBlockings);
        for (int index = 0; index < blogBlockings.size(); index++) {
            when(mockBlogBlockingToBlogBlockingDTOMapper.map(blogBlockings.get(index)))
                    .thenReturn(expectedResult.get(index));
        }

        // Run the test
        final List<BlogBlockingDTO> result = blogBlockingServiceImplUnderTest.findByBlogAndBlockedUserDisplayedUsernameContains(blogId, username, page, pageSize);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFindNotEndedByBlogAndBlockedUserDisplayedUsernameContains() {
        // Setup
        final Long blogId = 0L;
        final String username = "username";
        final int page = 0;
        final int pageSize = 5;
        final List<BlogBlockingDTO> expectedResult = Collections.singletonList(
                BlogBlockingDTO.builder().id(1L).build()
        );
        final List<BlogBlocking> blogBlockings = Collections.singletonList(
                BlogBlocking.builder().id(1L).build()
        );
        final Blog blog = Blog.builder().id(1L).build();
        final PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.Direction.DESC, "id");
        final LocalDateTime now = LocalDateTime.now();
        when(mockBlogRepository.findById(blogId)).thenReturn(Optional.of(blog));
        when(mockTimeStampProvider.now()).thenReturn(now);
        when(mockBlogBlockingRepository
                .findByBlogAndBlockedUserDisplayedNameContainsAndEndDateGreaterThan(
                        blog, username, Date.from(now.toInstant(ZoneOffset.UTC)), pageRequest
                )).thenReturn(blogBlockings);
        for (int index = 0; index < blogBlockings.size(); index++) {
            when(mockBlogBlockingToBlogBlockingDTOMapper.map(blogBlockings.get(index)))
                    .thenReturn(expectedResult.get(index));
        }

        // Run the test
        final List<BlogBlockingDTO> result = blogBlockingServiceImplUnderTest.findNotEndedByBlogAndBlockedUserDisplayedUsernameContains(blogId, username, page, pageSize);

        // Verify the results
        assertEquals(expectedResult, result);
    }
}
