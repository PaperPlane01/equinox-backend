package aphelion.service;

import aphelion.TestConstants;
import aphelion.exception.BlogBlockingNotFoundException;
import aphelion.mapper.BlogBlockingToBlogBlockingDTOMapper;
import aphelion.mapper.CreateBlogBlockingDTOToBlogBlockingMapper;
import aphelion.model.domain.Blog;
import aphelion.model.domain.BlogBlocking;
import aphelion.model.domain.User;
import aphelion.model.dto.BlogBlockingDTO;
import aphelion.model.dto.BlogMinifiedDTO;
import aphelion.model.dto.UserDTO;
import aphelion.repository.BlogBlockingRepository;
import aphelion.repository.BlogRepository;
import aphelion.repository.UserRepository;
import aphelion.service.impl.BlogBlockingServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class BlogBlockingServiceTest {
    @InjectMocks
    BlogBlockingServiceImpl blogBlockingService;

    @Mock
    BlogBlockingRepository blogBlockingRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    BlogRepository blogRepository;

    @Mock
    BlogBlockingToBlogBlockingDTOMapper blogBlockingToBlogBlockingDTOMapper;

    @Mock
    CreateBlogBlockingDTOToBlogBlockingMapper createBlogBlockingDTOToBlogBlockingMapper;

    @Before
    public void setUp() throws ParseException {
        Date startDate = new SimpleDateFormat(TestConstants.DATE_PATTERN)
                .parse("11/12/2018 14:13:14");
        Date endDate = Date.from(LocalDateTime.now().plusDays(2).toInstant(ZoneOffset.UTC));
        BlogBlocking blogBlocking = BlogBlocking.builder()
                .id(1)
                .blog(Blog.builder().id(1).build())
                .blockedBy(User.builder().id(1).build())
                .blockedUser(User.builder().id(2).build())
                .startDate(startDate)
                .endDate(endDate)
                .build();

        BlogBlockingDTO blogBlockingDTO = BlogBlockingDTO.builder()
                .id(1L)
                .blog(BlogMinifiedDTO.builder().id(1).build())
                .blockedBy(UserDTO.builder().id(1L).build())
                .blockedUser(UserDTO.builder().id(1L).build())
                .startDate(startDate)
                .endDate(endDate)
                .build();


        when(blogBlockingRepository.findById(1L)).thenReturn(Optional.of(blogBlocking));
        when(blogBlockingRepository.findById(2L)).thenReturn(Optional.empty());
        when(blogBlockingToBlogBlockingDTOMapper.map(blogBlocking)).thenReturn(blogBlockingDTO);
    }

    @Test
    public void whenFindingBlogBlockingById_thenIdEqualsToRequested() {
        BlogBlockingDTO blogBlockingDTO = blogBlockingService.findById(1L);
        Assert.assertEquals(1L, (long) blogBlockingDTO.getId());
    }

    @Test(expected = BlogBlockingNotFoundException.class)
    public void whenTryingToFindNotExistingBlogBlocking_thenExceptionIsThrown() {
        blogBlockingService.findById(2L);
    }
}
