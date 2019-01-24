package aphelion.controller;

import aphelion.model.dto.BlogBlockingDTO;
import aphelion.model.dto.BlogDTO;
import aphelion.model.dto.BlogMinifiedDTO;
import aphelion.model.dto.BlogPostDTO;
import aphelion.model.dto.BlogPostMinifiedDTO;
import aphelion.model.dto.CreateBlogManagerDTO;
import aphelion.model.dto.ManagedBlogDTO;
import aphelion.model.dto.BlogManagerDTO;
import aphelion.model.dto.ManagedBlogWithUserDTO;
import aphelion.model.dto.RestoreOrDeleteBlogDTO;
import aphelion.model.dto.SubscriptionWithUserDTO;
import aphelion.model.dto.UpdateBlogDTO;
import aphelion.model.dto.UpdateBlogManagerDTO;
import aphelion.model.dto.UserDTO;
import aphelion.model.dto.UserMinifiedDTO;
import aphelion.service.BlogBlockingService;
import aphelion.service.BlogPostService;
import lombok.RequiredArgsConstructor;
import aphelion.model.dto.CreateBlogDTO;
import aphelion.service.BlogManagerService;
import aphelion.service.BlogService;
import aphelion.service.SubscriptionService;
import aphelion.annotation.IncreaseNumberOfViews;
import aphelion.annotation.IncreaseNumberOfViews.For;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/blogs")
@RequiredArgsConstructor
public class BlogController {
    private final BlogService blogService;
    private final BlogPostService blogPostService;
    private final SubscriptionService subscriptionService;
    private final BlogManagerService blogManagerService;
    private final BlogBlockingService blogBlockingService;

    @PreAuthorize("hasRole('USER') and @blogPermissionResolver.canCreateBlog()")
    @PostMapping
    public BlogDTO save(@RequestBody @Valid CreateBlogDTO createBlogDTO) {
        return blogService.save(createBlogDTO);
    }

    @PreAuthorize("hasRole('USER') and @blogPermissionResolver.canUpdateBlog(#id)")
    @PutMapping("/{id}")
    public BlogDTO update(@PathVariable("id") Long id,
                          @RequestBody @Valid UpdateBlogDTO updateBlogDTO) {
        return blogService.update(id, updateBlogDTO);
    }

    @PreAuthorize("(hasRole('USER') and @blogPermissionResolver.canDeleteBlog(#id))")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        blogService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public BlogDTO findById(@PathVariable("id") Long id) {
        return blogService.findById(id);
    }

    @GetMapping("/{blogId}/subscribers")
    public List<UserMinifiedDTO> findSubscribersByBlog(@PathVariable("blogId") Long blogId,
                                                       @RequestParam("page") Optional<Integer> page,
                                                       @RequestParam("pageSize")
                                                                   Optional<Integer> pageSize,
                                                       @RequestParam("sortBy") Optional<String> sortBy) {
        return subscriptionService.findSubscribersByBlog(blogId, page.orElse(1),
                pageSize.orElse(50), sortBy.orElse("id"));
    }

    @GetMapping("/{blogId}/subscriptions")
    public List<SubscriptionWithUserDTO> findSubscriptionsByBlog(@PathVariable("blogId") Long blogId,
                                                                 @RequestParam("page")
                                                                         Optional<Integer> page,
                                                                 @RequestParam("pageSize")
                                                                             Optional<Integer> pageSize,
                                                                 @RequestParam("sortingDirection")
                                                                             Optional<String> sortingDirection,
                                                                 @RequestParam("sortBy")
                                                                             Optional<String> sortBy) {
        return subscriptionService.findByBlog(blogId, page.orElse(0), pageSize.orElse(50),
                sortingDirection.orElse("asc"), sortBy.orElse("id"));
    }

    @GetMapping(value = "/{blogId}/subscriptions", params = {"username"})
    public List<SubscriptionWithUserDTO> findSubscriptionsByBlogAndUsername(
            @PathVariable("blogId") Long blogId,
            @RequestParam("username") String username,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("pageSize") Optional<Integer> pageSize,
            @RequestParam("sortingDirection") Optional<String> sortingDirection,
            @RequestParam("sortBy") Optional<String> sortBy) {
        return subscriptionService.findByBlogAndUsername(blogId, username, page.orElse(0),
                pageSize.orElse(50), sortingDirection.orElse("id"), sortBy.orElse("asc"));
    }

    @IncreaseNumberOfViews(For.MULTIPLE_BLOG_POSTS)
    @GetMapping("/{blogId}/blog-posts")
    public List<BlogPostDTO> findBlogPostsByBlog(@PathVariable("blogId") Long blogId,
                                                 @RequestParam("page") Optional<Integer> page,
                                                 @RequestParam("pageSize") Optional<Integer> pageSize,
                                                 @RequestParam("sortingDirection")
                                                             Optional<String> sortingDirection,
                                                 @RequestParam("sortBy")
                                                             Optional<String> sortBy) {
        return blogPostService.findByBlog(blogId, page.orElse(0), pageSize.orElse(10),
                sortingDirection.orElse("asc"), sortBy.orElse("id"));
    }

    @GetMapping("/{blogId}/blog-posts/deleted")
    public List<BlogPostMinifiedDTO> findDeletedByBlog(@PathVariable("blogId") Long blogId,
                                                       @RequestParam("page") Optional<Integer> page,
                                                       @RequestParam("pageSize")
                                                                   Optional<Integer> pageSize,
                                                       @RequestParam("sortingDirection")
                                                                   Optional<String> sortingDirection,
                                                       @RequestParam("sortBy") Optional<String> sortBy) {
        return blogPostService.findDeletedByCurrentUserInBlog(blogId, page.orElse(0),
                pageSize.orElse(10), sortingDirection.orElse("desc"),
                sortBy.orElse("id"));
    }

    @PreAuthorize("hasRole('USER') && @blogManagerPermissionResolver.canCreateBlogManager(#blogId)")
    @PostMapping("/{blogId}/managers")
    public ManagedBlogDTO saveBlogManager(@PathVariable("blogId") Long blogId,
                                          @RequestBody @Valid CreateBlogManagerDTO createBlogManagerDTO) {
        createBlogManagerDTO.setBlogId(blogId);
        return blogManagerService.save(createBlogManagerDTO);
    }

    @PreAuthorize("hasRole('USER') && @blogManagerPermissionResolver.canDeleteBlogManager(#blogId, #managerId)")
    @DeleteMapping("/{blogId}/managers/{managerId}")
    public ResponseEntity<?> deleteBlogManager(@PathVariable("blogId") Long blogId,
                                    @PathVariable("managerId") Long managerId) {
        blogManagerService.delete(managerId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('USER') && @blogManagerPermissionResolver.canUpdateBlogManager(#blogId, #managerId)")
    @PatchMapping("/{blogId}/managers/{managerId}")
    public ManagedBlogDTO updateBlogManager(@PathVariable("blogId") Long blogId,
                                            @PathVariable("managerId") Long managerId,
                                            @RequestBody @Valid UpdateBlogManagerDTO updateBlogManagerDTO) {
        return blogManagerService.update(managerId, updateBlogManagerDTO);
    }

    @PreAuthorize("@blogManagerPermissionResolver.canSeeBlogManagers(#blogId)")
    @GetMapping("/{blogId}/managers/{managerId}")
    public BlogManagerDTO findBlogManagerById(@PathVariable("blogId") Long blogId,
                                              @PathVariable("managerId") Long managerId) {
        return blogManagerService.findById(managerId);
    }

    @PreAuthorize("@blogManagerPermissionResolver.canSeeBlogManagers(#blogId)")
    @GetMapping("/{blogId}/managers")
    public List<ManagedBlogWithUserDTO> findBlogManagersByBlog(@PathVariable("blogId") Long blogId,
                                                               @RequestParam("page")
                                                                       Optional<Integer> page,
                                                               @RequestParam("pageSize")
                                                                       Optional<Integer> pageSize,
                                                               @RequestParam("sortingDirection")
                                                                       Optional<String> sortingDirection,
                                                               @RequestParam("sortBy")
                                                                       Optional<String> sortBy) {
        return blogManagerService.findByBlog(blogId, page.orElse(0), pageSize.orElse(10),
                sortingDirection.orElse("asc"), sortBy.orElse("id"));
    }

    @PreAuthorize("@blogManagerPermissionResolver.canSeeBlogManagers(#blogId)")
    @GetMapping(value = "/{blogId}/managers", params = {"username"})
    public List<ManagedBlogWithUserDTO> findBlogManagersByBlogAndUsername(
            @PathVariable("blogId") Long blogId,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("pageSize") Optional<Integer> pageSize,
            @RequestParam("sortingDirection") Optional<String> sortingDirection,
            @RequestParam("sortBy") Optional<String> sortBy,
            @RequestParam("username") String username) {
        return blogManagerService.findByBlogAndDisplayedUsername(blogId, username,
                page.orElse(0), pageSize.orElse(30), sortingDirection.orElse("asc"),
                sortBy.orElse("id"));
    }

    @PreAuthorize("hasRole('USER') && @blogManagerPermissionResolver.canSeeBlogManagers(#blogId)")
    @GetMapping("/{blogId}/owner")
    public UserDTO findOwnerOfBlog(@PathVariable("blogId") Long blogId) {
        return blogService.findById(blogId).getOwner();
    }

    @PreAuthorize("hasRole('USER') && @blogPermissionResolver.canRestoreOrDeleteBlog(#id, " +
            "#restoreOrDeleteBlogDTO)")
    @PatchMapping("/{id}")
    public Object restoreOrDeleteBlog(@PathVariable("id") Long id,
                                      @RequestBody @Valid
                                              RestoreOrDeleteBlogDTO restoreOrDeleteBlogDTO) {
        if (restoreOrDeleteBlogDTO.getDeleted()) {
            blogService.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(blogService.restore(id));
        }
    }

    @PreAuthorize("hasRole('USER') and @blogBlockingPermissionResolver.canViewBlogBlockingsInBlog(#id)")
    @GetMapping("/{id}/blockings")
    public List<BlogBlockingDTO> findBlogBlockingsByBlog(@PathVariable("id") Long id,
                                                         @RequestParam("page") Optional<Integer> page,
                                                         @RequestParam("pageSize") Optional<Integer> pageSize) {
        return blogBlockingService.findByBlog(id, page.orElse(0), pageSize.orElse(20));
    }

    @PreAuthorize("hasRole('USER') and @blogBlockingPermissionResolver.canViewBlogBlockingsInBlog(#id)")
    @GetMapping("/{id}/blockings/not-ended")
    public List<BlogBlockingDTO> findNotEndedBlogBlockingsByBlog(@PathVariable("id") Long id,
                                                                 @RequestParam("page") Optional<Integer> page,
                                                                 @RequestParam("pageSize")
                                                                         Optional<Integer> pageSize) {
        return blogBlockingService.findNotEndedByBlog(id, page.orElse(0), pageSize.orElse(20));
    }

    @PreAuthorize("hasRole('USER') && @blogBlockingPermissionResolver.canViewBlogBlockingsInBlog(#id)")
    @GetMapping(value = "/{id}/blockings", params = {"blockedUserDisplayedUsername"})
    public List<BlogBlockingDTO> findBlogBlockingsByBlogAndBlockedUserDisplayedUsername(
            @PathVariable("id") Long id,
            @RequestParam("blockedUserDisplayedUsername") String blockedUserDisplayedUsername,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("pageSize") Optional<Integer> pageSize) {
        return blogBlockingService.findByBlogAndBlockedUserDisplayedUsernameContains(
                id, blockedUserDisplayedUsername, page.orElse(0), pageSize.orElse(20)
        );
    }

    @PreAuthorize("hasRole('USER') && @blogBlockingPermissionResolver.canViewBlogBlockingsInBlog(#id)")
    @GetMapping(value = "/{id}/blockings/not-ended", params = {"blockedUserDisplayedUsername"})
    public List<BlogBlockingDTO> findNotEndedBlogBlockingsByBlogAndBlockedUserDisplayedUsername(
            @PathVariable("id") Long id,
            @RequestParam("blockedUserDisplayedUsername") String blockedUserDisplayedUsername,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("pageSize") Optional<Integer> pageSize) {
        return blogBlockingService.findNotEndedByBlogAndBlockedUserDisplayedUsernameContains(
                id, blockedUserDisplayedUsername, page.orElse(0), pageSize.orElse(20)
        );
    }

    @GetMapping("/{id}/minified")
    public BlogMinifiedDTO findMinifiedById(@PathVariable("id") Long id) {
        return blogService.findMinifiedById(id);
    }

    @IncreaseNumberOfViews(For.MULTIPLE_BLOG_POSTS)
    @GetMapping("/{id}/blog-posts/pinned")
    public List<BlogPostDTO> findPinnedBlogPostsByBlog(@PathVariable("id") Long id) {
        return blogPostService.findPinnedByBlog(id);
    }
}