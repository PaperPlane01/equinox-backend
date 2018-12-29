package aphelion.security.access;

import aphelion.model.domain.BlogRole;
import aphelion.model.dto.BlogBlockingDTO;
import aphelion.model.dto.BlogDTO;
import aphelion.model.dto.BlogMinifiedDTO;
import aphelion.model.dto.CurrentUserDTO;
import aphelion.model.dto.ManagedBlogDTO;
import aphelion.model.dto.UserDTO;
import aphelion.service.BlogBlockingService;
import aphelion.service.BlogService;
import aphelion.service.UserService;
import lombok.var;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class BlogBlockingPermissionResolverTest {
    @InjectMocks
    private BlogBlockingPermissionResolver blogBlockingPermissionResolver;

    @Mock
    private BlogBlockingService blogBlockingService;

    @Mock
    private BlogService blogService;

    @Mock
    private UserService userService;

    private static final long CURRENT_USER_ID = 1;
    private static final long FIRST_BLOG_ID = 1;
    private static final long SECOND_BLOG_ID = 2;
    private static final long THIRD_BLOG_ID = 3;
    private static final long FIRST_BLOG_BLOCKING_ID = 4;
    private static final long SECOND_BLOG_BLOCKING_ID = 5;
    private static final long THIRD_BLOG_BLOCKING_ID = 6;

    @Before
    public void setUp() {
        var currentUser = CurrentUserDTO.builder()
                .id(CURRENT_USER_ID)
                .managedBlogs(Collections.singletonList(
                        ManagedBlogDTO.builder()
                                .blogId(FIRST_BLOG_ID)
                                .blogRole(BlogRole.MODERATOR)
                                .build()))
                .ownedBlogs(Collections.singletonList(THIRD_BLOG_ID))
                .build();
        var firstBlog = BlogDTO.builder()
                .id(FIRST_BLOG_ID)
                .owner(UserDTO.builder().id(3L).build())
                .build();
        var secondBlog = BlogDTO.builder()
                .id(SECOND_BLOG_ID)
                .owner(UserDTO.builder().id(4L).build())
                .build();
        var thirdBlog = BlogDTO.builder()
                .id(THIRD_BLOG_ID)
                .owner(UserDTO.builder().id(CURRENT_USER_ID).build())
                .build();
        var firstBlogBlocking = BlogBlockingDTO.builder()
                .id(FIRST_BLOG_BLOCKING_ID)
                .blog(BlogMinifiedDTO.builder().id(FIRST_BLOG_ID).build())
                .build();
        var secondBlogBlocking = BlogBlockingDTO.builder()
                .id(SECOND_BLOG_BLOCKING_ID)
                .blog(BlogMinifiedDTO.builder().id(SECOND_BLOG_ID).build())
                .build();
        var thirdBlogBlocking = BlogBlockingDTO.builder()
                .id(THIRD_BLOG_BLOCKING_ID)
                .blog(BlogMinifiedDTO.builder().id(THIRD_BLOG_ID).build())
                .build();

        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(blogService.findById(FIRST_BLOG_ID)).thenReturn(firstBlog);
        when(blogService.findById(SECOND_BLOG_ID)).thenReturn(secondBlog);
        when(blogService.findById(THIRD_BLOG_ID)).thenReturn(thirdBlog);
        when(blogBlockingService.findById(FIRST_BLOG_BLOCKING_ID)).thenReturn(firstBlogBlocking);
        when(blogBlockingService.findById(SECOND_BLOG_BLOCKING_ID)).thenReturn(secondBlogBlocking);
        when(blogBlockingService.findById(THIRD_BLOG_BLOCKING_ID)).thenReturn(thirdBlogBlocking);
    }

    @Test
    public void whenCurrentUserIsNorBlogOwnerNeitherBlogManager_thenBlogBlockingPermissionsDenied() {
        assertFalse(blogBlockingPermissionResolver.canBlockUsersInBlog(SECOND_BLOG_ID));
        assertFalse(blogBlockingPermissionResolver.canUnblockUser(SECOND_BLOG_BLOCKING_ID));
        assertFalse(blogBlockingPermissionResolver.canViewBlogBlockingsInBlog(SECOND_BLOG_ID));
        assertFalse(blogBlockingPermissionResolver.canUpdateBlocking(SECOND_BLOG_BLOCKING_ID));
        assertFalse(blogBlockingPermissionResolver.canViewBlogBlocking(SECOND_BLOG_BLOCKING_ID));
    }

    @Test
    public void whenCurrentUserIsBlogOwner_thenBlogBlockingPermissionsGranted() {
        assertTrue(blogBlockingPermissionResolver.canBlockUsersInBlog(THIRD_BLOG_ID));
        assertTrue(blogBlockingPermissionResolver.canUnblockUser(THIRD_BLOG_BLOCKING_ID));
        assertTrue(blogBlockingPermissionResolver.canViewBlogBlockingsInBlog(THIRD_BLOG_ID));
        assertTrue(blogBlockingPermissionResolver.canUpdateBlocking(THIRD_BLOG_BLOCKING_ID));
        assertTrue(blogBlockingPermissionResolver.canViewBlogBlocking(THIRD_BLOG_BLOCKING_ID));
    }

    @Test
    public void whenCurrentUserIsBlogManager_thenBlogBlockingPermissionsGranted() {
        assertTrue(blogBlockingPermissionResolver.canBlockUsersInBlog(FIRST_BLOG_ID));
        assertTrue(blogBlockingPermissionResolver.canUnblockUser(FIRST_BLOG_BLOCKING_ID));
        assertTrue(blogBlockingPermissionResolver.canViewBlogBlockingsInBlog(FIRST_BLOG_ID));
        assertTrue(blogBlockingPermissionResolver.canUpdateBlocking(FIRST_BLOG_BLOCKING_ID));
        assertTrue(blogBlockingPermissionResolver.canViewBlogBlocking(FIRST_BLOG_BLOCKING_ID));
    }
}
