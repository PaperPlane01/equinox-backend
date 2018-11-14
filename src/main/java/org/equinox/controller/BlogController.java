package org.equinox.controller;

import lombok.RequiredArgsConstructor;
import org.equinox.model.dto.BlogBlockingDTO;
import org.equinox.model.dto.BlogDTO;
import org.equinox.model.dto.BlogMinifiedDTO;
import org.equinox.model.dto.BlogPostDTO;
import org.equinox.model.dto.BlogPostMinifiedDTO;
import org.equinox.model.dto.CreateBlogDTO;
import org.equinox.model.dto.CreateBlogManagerDTO;
import org.equinox.model.dto.ManagedBlogDTO;
import org.equinox.model.dto.ManagedBlogWithUserDTO;
import org.equinox.model.dto.RestoreOrDeleteBlogDTO;
import org.equinox.model.dto.SubscriptionWithUserDTO;
import org.equinox.model.dto.UpdateBlogDTO;
import org.equinox.model.dto.UpdateBlogManagerDTO;
import org.equinox.model.dto.UserDTO;
import org.equinox.model.dto.UserMinifiedDTO;
import org.equinox.service.BlogBlockingService;
import org.equinox.service.BlogManagerService;
import org.equinox.service.BlogPostService;
import org.equinox.service.BlogService;
import org.equinox.service.SubscriptionService;
import org.equinox.annotation.IncreaseNumberOfViews;
import org.equinox.annotation.IncreaseNumberOfViews.For;
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

    @PreAuthorize("hasRole('USER') && @blogManagerPermissionResolver.canDeleteBlogManager(#blogId)")
    @DeleteMapping("/{blogId}/managers/{managerId}")
    public ResponseEntity<?> delete(@PathVariable("blogId") Long blogId,
                                    @PathVariable("managerId") Long managerId) {
        blogManagerService.delete(managerId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('USER') && @blogManagerPermissionResolver.canUpdateBlogManager(#blogId)")
    @PatchMapping("/{blogId}/managers/{managerId}")
    public ManagedBlogDTO updateBlogManager(@PathVariable("blogId") Long blogId,
                                            @PathVariable("managerId") Long managerId,
                                            @RequestBody @Valid UpdateBlogManagerDTO updateBlogManagerDTO) {
        return blogManagerService.update(managerId, updateBlogManagerDTO);
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

    @PreAuthorize("hasRole('USER') && @blogBlockingPermissionResolver.canViewBlogBlockingsInBlog(#id)")
    @GetMapping("/{id}/blockings")
    public List<BlogBlockingDTO> findBlogBlockingsByBlog(@PathVariable("id") Long id,
                                                         @RequestParam("page") Optional<Integer> page,
                                                         @RequestParam("pageSize") Optional<Integer> pageSize) {
        return blogBlockingService.findByBlog(id, page.orElse(0), pageSize.orElse(20));
    }

    @PreAuthorize("hasRole('USER') && blogBlockingPermissionResolver.canViewBlogBlockingsInBlog(#id)")
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
            Optional<Integer> page,
            Optional<Integer> pageSize) {
        return blogBlockingService.findByBlogAndBlockedUserDisplayedUsernameContains(
                id, blockedUserDisplayedUsername, page.orElse(0), pageSize.orElse(20)
        );
    }

    @PreAuthorize("hasRole('USER') && @blogBlockingPermissionResolver.canViewBlogBlockingsInBlog(#id)")
    @GetMapping(value = "/{id}/blockings/not-ended", params = {"blockedUserDisplayedUsername"})
    public List<BlogBlockingDTO> findNotEndedBlogBlockingsByBlogAndBlockedUserDisplayedUsername(
            @PathVariable("id") Long id,
            @RequestParam("blockedUserDisplayedUsername") String blockedUserDisplayedUsername,
            Optional<Integer> page,
            Optional<Integer> pageSize) {
        return blogBlockingService.findNotEndedByBlogAndBlockedUserDisplayedUsernameContains(
                id, blockedUserDisplayedUsername, page.orElse(0), pageSize.orElse(20)
        );
    }

    @GetMapping("/{id}/minified")
    public BlogMinifiedDTO findMinifiedById(@PathVariable("id") Long id) {
        return blogService.findMinifiedById(id);
    }
}