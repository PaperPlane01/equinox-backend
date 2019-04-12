package aphelion.controller;

import aphelion.annotation.IncreaseNumberOfViews;
import aphelion.annotation.IncreaseNumberOfViews.For;
import aphelion.model.dto.BlogBlockingDTO;
import aphelion.model.dto.BlogDTO;
import aphelion.model.dto.BlogManagerDTO;
import aphelion.model.dto.BlogMinifiedDTO;
import aphelion.model.dto.BlogPostDTO;
import aphelion.model.dto.BlogPostMinifiedDTO;
import aphelion.model.dto.CreateBlogDTO;
import aphelion.model.dto.CreateBlogManagerDTO;
import aphelion.model.dto.ManagedBlogDTO;
import aphelion.model.dto.ManagedBlogWithUserDTO;
import aphelion.model.dto.RestoreOrDeleteBlogDTO;
import aphelion.model.dto.SubscriptionWithUserDTO;
import aphelion.model.dto.UpdateBlogDTO;
import aphelion.model.dto.UpdateBlogManagerDTO;
import aphelion.model.dto.UserDTO;
import aphelion.model.dto.UserMinifiedDTO;
import aphelion.service.BlogBlockingService;
import aphelion.service.BlogManagerService;
import aphelion.service.BlogPostService;
import aphelion.service.BlogService;
import aphelion.service.SubscriptionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
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
@Api(value = "Blog controller", description = "Operations over blogs")
public class BlogController {
    private final BlogService blogService;
    private final BlogPostService blogPostService;
    private final SubscriptionService subscriptionService;
    private final BlogManagerService blogManagerService;
    private final BlogBlockingService blogBlockingService;

    @ApiOperation(
            value = "Creates new blog",
            notes = "Requires current user to have \"ROLE_USER\" role, and not being blocked " +
                    "globally"
    )
    @ApiResponses({
            @ApiResponse(
                    code = 201,
                    message = "Blog has been created successfully",
                    response = BlogDTO.class
            ),
            @ApiResponse(code = 400, message = "Request is invalid"),
            @ApiResponse(code = 401, message = "Access token has expired"),
            @ApiResponse(
                    code = 403,
                    message = "Current user cannot perform this operation. (Probably, it means " +
                            "than user has been blocked globally"
            )
    })
    @ApiImplicitParams({
            @ApiImplicitParam(
                    value = "Access token",
                    name = "Authorization",
                    required = true,
                    paramType = "header",
                    format = "Bearer {access_token}"
            )
    })
    @PreAuthorize("hasRole('USER') and @blogPermissionResolver.canCreateBlog()")
    @PostMapping
    public BlogDTO save(
            @ApiParam(
                    name = "Create blog request" ,
                    value = "Object with data about created blog post",
                    required = true
            )
            @RequestBody @Valid CreateBlogDTO createBlogDTO) {
        return blogService.save(createBlogDTO);
    }

    @ApiOperation(
            value = "Updates blog with specified id",
            notes = "Requires current user to be a owner of specified blog"
    )
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Successfully updated blog",
                    response = BlogDTO.class
            ),
            @ApiResponse(code = 400, message = "Invalid request"),
            @ApiResponse(code = 401, message = "Access token has expired"),
            @ApiResponse(code = 403, message = "Current user can't do this operation"),
            @ApiResponse(code = 404, message = "Blog with such is not found")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(
                    value = "Access token",
                    name = "Authorization",
                    required = true,
                    paramType = "header"
            )
    })
    @PreAuthorize("hasRole('USER') and @blogPermissionResolver.canUpdateBlog(#id)")
    @PutMapping("/{id}")
    public BlogDTO update(
            @ApiParam(value = "Id of blog to be updated", required = true)
            @PathVariable("id") Long id,
            @ApiParam(value = "Update blog request", required = true)
            @RequestBody @Valid UpdateBlogDTO updateBlogDTO) {
        return blogService.update(id, updateBlogDTO);
    }

    @ApiOperation(
            value = "Deletes blog with specified id",
            notes = "Requires current user to be owner of specified blog or have " +
                    "<code>ROLE_ADMIN</code> role"
    )
    @ApiResponses({
            @ApiResponse(code = 204, message = "Successfully deleted blog with specified id"),
            @ApiResponse(code = 401, message = "Access token has expired"),
            @ApiResponse(code = 403, message = "Current user can't perform this operation"),
            @ApiResponse(code = 404, message = "Blog with specified id not found")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(
                    value = "Access token",
                    name = "Authorization",
                    required = true,
                    paramType = "header"
            )
    })
    @PreAuthorize("(hasRole('USER') and @blogPermissionResolver.canDeleteBlog(#id))")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        blogService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation("Retrieves blog with specified id")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Successfully retrieved blog with specified id",
                    response = BlogDTO.class
            ),
            @ApiResponse(code = 401, message = "Access token has expired"),
            @ApiResponse(code = 404, message = "Blog with specified id not found")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(
                    value = "Access token",
                    name = "Authorization",
                    paramType = "header"
            )
    })
    @GetMapping("/{id}")
    public BlogDTO findById(
            @ApiParam(value = "Id of blog", required = true)
            @PathVariable("id") Long id) {
        return blogService.findById(id);
    }

    @ApiOperation("Retrieves subscribers of blog with specified id")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Successfully retrieved subscribers of blog",
                    response = UserMinifiedDTO[].class
            ),
            @ApiResponse(code = 401, message = "Access token has expired"),
            @ApiResponse(code = 404, message = "Blog with specified id not found")
    })
    @GetMapping("/{blogId}/subscribers")
    public List<UserMinifiedDTO> findSubscribersByBlog(
            @ApiParam(value = "Id of blog", required = true)
            @PathVariable("blogId") Long blogId,
            @ApiParam("page")
            @RequestParam("page") Optional<Integer> page,
            @ApiParam("Page size. Max size is 150")
            @RequestParam("pageSize") Optional<Integer> pageSize,
            @ApiParam("Sorting property. Allowed properties are <code>[id, random]</code>")
            @RequestParam("sortBy") Optional<String> sortBy) {
        return subscriptionService.findSubscribersByBlog(
                blogId,
                page.orElse(1),
                pageSize.orElse(50),
                sortBy.orElse("id")
        );
    }

    @ApiOperation("Retrieves subscriptions of blog with specified id")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Successfully retrieved list of subscriptions",
                    response = SubscriptionWithUserDTO[].class
            ),
            @ApiResponse(code = 401, message = "Access token has expired"),
            @ApiResponse(code = 404, message = "Blog with specified id not found")
    })
    @GetMapping("/{blogId}/subscriptions")
    public List<SubscriptionWithUserDTO> findSubscriptionsByBlog(
            @ApiParam(value = "Id of blog", required = true)
            @PathVariable("blogId") Long blogId,
            @ApiParam("Page. Default page is 0")
            @RequestParam("page") Optional<Integer> page,
            @ApiParam("Page size. Max size is 150, default size is 50")
            @RequestParam("pageSize") Optional<Integer> pageSize,
            @ApiParam("Sorting direction. Default property is <code>asc</code>, " +
                    "allowed properties are <code>[asc, desc]</code>")
            @RequestParam("sortingDirection") Optional<String> sortingDirection,
            @ApiParam("Property of sorting. Default property is <code>id</code>, " +
                    "allow properties are <code>[id]</code>")
            @RequestParam("sortBy") Optional<String> sortBy) {
        return subscriptionService.findByBlog(
                blogId,
                page.orElse(0),
                pageSize.orElse(50),
                sortingDirection.orElse("asc"),
                sortBy.orElse("id")
        );
    }

    @ApiOperation("Retrieves subscriptions of blog with specified id and username like given")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Successfully retrieved list of subscriptions",
                    response = SubscriptionWithUserDTO[].class
            ),
            @ApiResponse(code = 401, message = "Access token has expired"),
            @ApiResponse(code = 404, message = "Blog with specified id not found")
    })
    @GetMapping(value = "/{blogId}/subscriptions", params = {"username"})
    public List<SubscriptionWithUserDTO> findSubscriptionsByBlogAndUsername(
            @ApiParam(value = "Id of blog", required = true)
            @PathVariable("blogId") Long blogId,
            @ApiParam(value = "Username to search", required = true)
            @RequestParam("username") String username,
            @RequestParam("page") Optional<Integer> page,
            @ApiParam("Page size. Max size is 150, default size is 50")
            @RequestParam("pageSize") Optional<Integer> pageSize,
            @ApiParam("Sorting direction. Default property is <code>asc</code>, " +
                    "allowed properties are <code>[asc, desc]</code>")
            @RequestParam("sortingDirection") Optional<String> sortingDirection,
            @ApiParam("Property of sorting. Default property is <code>id</code>, " +
                    "allow properties are <code>[id]</code>")
            @RequestParam("sortBy") Optional<String> sortBy) {
        return subscriptionService.findByBlogAndUsername(
                blogId,
                username,
                page.orElse(0),
                pageSize.orElse(50),
                sortingDirection.orElse("id"),
                sortBy.orElse("asc")
        );
    }

    @ApiOperation(
            value = "Retrieves blog posts of blog with specified id",
            notes = "This endpoint does not increase number of views for pinned blog post. " +
                    "It is assumed that pinned blog post will be retrieved using " +
                    "<code>/api/blogs/{blogId}/blog-posts/pinned</code> endpoint"
    )
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Successfully retrieved list of blog posts",
                    response = BlogPostDTO[].class
            ),
            @ApiResponse(code = 401, message = "Access token has expired"),
            @ApiResponse(code = 404, message = "Blog with specified id not found")
    })
    @IncreaseNumberOfViews(value = For.MULTIPLE_BLOG_POSTS, increaseForPinnedBlogPosts = false)
    @GetMapping("/{blogId}/blog-posts")
    public List<BlogPostDTO> findBlogPostsByBlog(
            @ApiParam("Id of blog")
            @PathVariable("blogId") Long blogId,
            @RequestParam("page") Optional<Integer> page,
            @ApiParam("Page size. Max size is 50, default size is 10")
            @RequestParam("pageSize") Optional<Integer> pageSize,
            @ApiParam("Sorting direction. Default property is <code>asc</code>, " +
                    "allowed properties are <code>[asc, desc]</code>")
            @RequestParam("sortingDirection") Optional<String> sortingDirection,
            @ApiParam("Property of sorting. Default property is <code>id</code>, " +
                    "allow properties are <code>[id, createdAt]</code>")
            @RequestParam("sortBy") Optional<String> sortBy) {
        return blogPostService.findByBlog(
                blogId, page.orElse(0),
                pageSize.orElse(10),
                sortingDirection.orElse("asc"),
                sortBy.orElse("id")
        );
    }

    @GetMapping("/{blogId}/blog-posts/deleted")
    public List<BlogPostMinifiedDTO> findDeletedByBlog(@PathVariable("blogId") Long blogId,
                                                       @RequestParam("page") Optional<Integer> page,
                                                       @RequestParam("pageSize")
                                                                   Optional<Integer> pageSize,
                                                       @RequestParam("sortingDirection")
                                                                   Optional<String> sortingDirection,
                                                       @RequestParam("sortBy") Optional<String> sortBy) {
        return blogPostService.findDeletedByCurrentUserInBlog(
                blogId,
                page.orElse(0),
                pageSize.orElse(10),
                sortingDirection.orElse("desc"),
                sortBy.orElse("id")
        );
    }

    @ApiOperation(
            value = "Creates new blog manager in blog with specified id",
            notes = "Requires user to be owner of blog"
    )
    @ApiResponses({
            @ApiResponse(
                    code = 201,
                    message = "Blog manager successfully created",
                    response = ManagedBlogDTO.class
            ),
            @ApiResponse(code = 400, message = "Request is invalid"),
            @ApiResponse(code = 401, message = "Access token has expired"),
            @ApiResponse(code = 403, message = "Current user can't do this operation"),
            @ApiResponse(code = 404, message = "Blog with suck id not found"),
            @ApiResponse(code = 409, message = "User already manages this blog")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(
                    value = "Access token",
                    name = "Authorization",
                    required = true,
                    paramType = "header"
            )
    })
    @PreAuthorize("hasRole('USER') && @blogManagerPermissionResolver.canCreateBlogManager(#blogId)")
    @PostMapping("/{blogId}/managers")
    public ManagedBlogDTO saveBlogManager(
            @ApiParam(value = "Id of blog", required = true)
            @PathVariable("blogId") Long blogId,
            @ApiParam(
                    name = "Create blog manager request",
                    value = "New blog manager",
                    required = true
            )
            @RequestBody @Valid CreateBlogManagerDTO createBlogManagerDTO) {
        createBlogManagerDTO.setBlogId(blogId);
        return blogManagerService.save(createBlogManagerDTO);
    }

    @ApiOperation(
            value = "Deletes blog manager",
            notes = "Requires current user to be owner of the blog"
    )
    @ApiResponses({
            @ApiResponse(code = 204, message = "Blog manager has been deleted successfully"),
            @ApiResponse(code = 401, message = "Access token has expired"),
            @ApiResponse(code = 403, message = "Current user can't to this operation"),
            @ApiResponse(
                    code = 404,
                    message = "Either blog with id specified in <code>blogId</code> path " +
                            "variable, or blog manager with id specified in " +
                            "<code>managerId</code> path variable could not be found"
            )
    })
    @ApiImplicitParams({
            @ApiImplicitParam(
                    value = "Access token",
                    name = "Authorization",
                    paramType = "header",
                    required = true
            )
    })
    @PreAuthorize("hasRole('USER') && @blogManagerPermissionResolver.canDeleteBlogManager(#blogId, #managerId)")
    @DeleteMapping("/{blogId}/managers/{managerId}")
    public ResponseEntity<?> deleteBlogManager(
            @ApiParam(value = "Id of blog", required = true)
            @PathVariable("blogId") Long blogId,
            @ApiParam(value = "Id of blog manager to be deleted", required = true)
            @PathVariable("managerId") Long managerId) {
        blogManagerService.delete(managerId);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(
            value = "Updates blog manager",
            notes = "Requires current user to be owner of the blog"
    )
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Blog manager has been updated successfully",
                    response = ManagedBlogDTO.class
            ),
            @ApiResponse(code = 400, message = "Invalid request"),
            @ApiResponse(code = 401, message = "Access token has expired"),
            @ApiResponse(code = 403, message = "Current user can't do this operation"),
            @ApiResponse(
                    code = 404,
                    message = "Either blog with id specified in <code>blogId</code> path " +
                            "variable, or blog manager with id specified in " +
                            "<code>managerId</code> path variable could not be found"
            )
    })
    @ApiImplicitParams({
            @ApiImplicitParam(
                    value = "Access token",
                    name = "Authorization",
                    required = true,
                    paramType = "header"
            )
    })
    @PreAuthorize("hasRole('USER') && @blogManagerPermissionResolver.canUpdateBlogManager(#blogId, #managerId)")
    @PatchMapping("/{blogId}/managers/{managerId}")
    public ManagedBlogDTO updateBlogManager(
            @ApiParam(value = "Id of blog", required = true)
            @PathVariable("blogId") Long blogId,
            @ApiParam(value = "Id of blog manager", required = true)
            @PathVariable("managerId") Long managerId,
            @ApiParam(value = "Updated blog manager", name = "Updated blog manager", required = true)
            @RequestBody @Valid UpdateBlogManagerDTO updateBlogManagerDTO) {
        return blogManagerService.update(managerId, updateBlogManagerDTO);
    }

    @ApiOperation(
            value = "Retrieves blog manager with specified id of blog with specified id",
            notes = "May require access token (it depends on blog's blog managers visibility " +
                    "level"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    value = "Access token",
                    name = "Authorization",
                    paramType = "header"
            )
    })
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Successfully retrieved blog manager",
                    response = BlogManagerDTO.class
            ),
            @ApiResponse(code = 400, message = "Invalid request"),
            @ApiResponse(code = 401, message = "Access token has expired"),
            @ApiResponse(code = 403, message = "Current user can't do this operation"),
            @ApiResponse(
                    code = 404,
                    message = "Either blog with id=<code>blogId</code> " +
                            "or blog manager with id=<code>managerId</code> not found"
            )
    })
    @PreAuthorize("@blogManagerPermissionResolver.canSeeBlogManagers(#blogId)")
    @GetMapping("/{blogId}/managers/{managerId}")
    public BlogManagerDTO findBlogManagerById(
            @ApiParam(value = "Id of blog", required = true)
            @PathVariable("blogId") Long blogId,
            @ApiParam(value = "Id of manager", required = true)
            @PathVariable("managerId") Long managerId) {
        return blogManagerService.findById(managerId);
    }

    @ApiOperation(
            value = "Retrieves managers of blog with specified id",
            notes = "May require access token (it depends on blog's blog managers visibility " +
                    "level"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    value = "Access token",
                    name = "Authorization",
                    paramType = "header"
            )
    })
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Successfully retrieved blog managers",
                    response = ManagedBlogWithUserDTO[].class
            ),
            @ApiResponse(code = 400, message = "Invalid request"),
            @ApiResponse(code = 401, message = "Access token has expired"),
            @ApiResponse(code = 403, message = "Current user can't do this operation"),
            @ApiResponse(code = 404, message = "Blog with specified id not found")
    })
    @PreAuthorize("@blogManagerPermissionResolver.canSeeBlogManagers(#blogId)")
    @GetMapping("/{blogId}/managers")
    public List<ManagedBlogWithUserDTO> findBlogManagersByBlog(
            @ApiParam(value = "Id of blog", required = true)
            @PathVariable("blogId") Long blogId,
            @ApiParam("Page number")
            @RequestParam("page") Optional<Integer> page,
            @ApiParam("Page size. Max size is 50")
            @RequestParam("pageSize") Optional<Integer> pageSize,
            @ApiParam("Sorting direction. Allowed values are <code>[asc, desc]</code")
            @RequestParam("sortingDirection") Optional<String> sortingDirection,
            @ApiParam("Sorting property. Allowed values are <code>[id]</code>")
            @RequestParam("sortBy") Optional<String> sortBy) {
        return blogManagerService.findByBlog(
                blogId,
                page.orElse(0),
                pageSize.orElse(10),
                sortingDirection.orElse("asc"),
                sortBy.orElse("id")
        );
    }

    @ApiOperation(
            value = "Retrieves blog manages of blog with specified id and " +
                    "username like specified in <code>username</code> request param",
            notes = "May require access token (it depends on blog's blog managers visibility " +
                    "level"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    value = "Access token",
                    name = "Authorization",
                    paramType = "header"
            )
    })
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Successfully retrieved blog managers",
                    response = BlogManagerDTO[].class
            ),
            @ApiResponse(code = 400, message = "Invalid request"),
            @ApiResponse(code = 401, message = "Access token has expired"),
            @ApiResponse(code = 403, message = "Current user can't do this operation"),
            @ApiResponse(code = 404, message = "Blog with specified id not found")
    })
    @PreAuthorize("@blogManagerPermissionResolver.canSeeBlogManagers(#blogId)")
    @GetMapping(value = "/{blogId}/managers", params = {"username"})
    public List<ManagedBlogWithUserDTO> findBlogManagersByBlogAndUsername(
            @ApiParam(value = "Id of blog", required = true)
            @PathVariable("blogId") Long blogId,
            @ApiParam("Page number")
            @RequestParam("page") Optional<Integer> page,
            @ApiParam("Page size. Max size is 50")
            @RequestParam("pageSize") Optional<Integer> pageSize,
            @ApiParam("Sorting direction. Allowed values are <code>[asc, desc]</code>")
            @RequestParam("sortingDirection") Optional<String> sortingDirection,
            @ApiParam("Sorting property. Allowed values are <code>[id]</code>")
            @RequestParam("sortBy") Optional<String> sortBy,
            @ApiParam(value = "Username to search")
            @RequestParam("username") String username) {
        return blogManagerService.findByBlogAndDisplayedUsername(
                blogId, username,
                page.orElse(0),
                pageSize.orElse(30),
                sortingDirection.orElse("asc"),
                sortBy.orElse("id")
        );
    }

    @ApiOperation(
            value = "Retrieves owner of blog with specified id",
            notes = "May require access token (it depends on blog's blog managers visibility " +
                    "level"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    value = "Access token",
                    name = "Authorization",
                    paramType = "header"
            )
    })
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Successfully retrieved owner of blog",
                    response = UserMinifiedDTO.class
            ),
            @ApiResponse(code = 400, message = "Invalid request"),
            @ApiResponse(code = 401, message = "Access token has expired"),
            @ApiResponse(code = 403, message = "Current user can't do this operation"),
            @ApiResponse(code = 404, message = "Blog with specified id not found")
    })
    @PreAuthorize("@blogManagerPermissionResolver.canSeeBlogManagers(#blogId)")
    @GetMapping("/{blogId}/owner")
    public UserMinifiedDTO findOwnerOfBlog(
            @ApiParam(value = "Id of blog", required = true)
            @PathVariable("blogId") Long blogId) {
        return blogService.findById(blogId).getOwner();
    }

    @ApiOperation(
            value = "Restores or deleted blog with specified id",
            notes = "In case of blog deleting, current user is required to be " +
                    "either blog's owner or to have <code>ROLE_ADMIN</code> role\n" +
                    "In case of blog restoring, current user is specified to be " +
                    "the same user who deleted the blog"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    value = "Access token",
                    name = "Authorization",
                    paramType = "header",
                    required = true
            )
    })
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Successfully restored blog",
                    response = BlogDTO.class
            ),
            @ApiResponse(
                    code = 204,
                    message = "Successfully deleted blog",
                    response = Void.class
            ),
            @ApiResponse(code = 400, message = "Invalid request"),
            @ApiResponse(code = 401, message = "Access token has expired"),
            @ApiResponse(code = 403, message = "Current user can't do this operation"),
            @ApiResponse(code = 404, message = "Blog with specified id not found")
    })
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

    @ApiOperation(
            value = "Retrieves blog blockings of blog with specified id",
            notes = "Requires current user to be either blog's owner " +
                    "or blog's manager"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    value = "Access token",
                    name = "Authorization",
                    paramType = "header",
                    required = true
            )
    })
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Successfully retrieved blog blockings of blog",
                    response = BlogBlockingDTO[].class
            ),
            @ApiResponse(code = 400, message = "Invalid request"),
            @ApiResponse(code = 401, message = "Access token has expired"),
            @ApiResponse(code = 403, message = "Current user can't do this operation"),
            @ApiResponse(code = 404, message = "Blog with specified id not found")
    })
    @PreAuthorize("hasRole('USER') and @blogBlockingPermissionResolver.canViewBlogBlockingsInBlog(#id)")
    @GetMapping("/{id}/blockings")
    public List<BlogBlockingDTO> findBlogBlockingsByBlog(
            @ApiParam(value = "Id of blog", required = true)
            @PathVariable("id") Long id,
            @ApiParam("Page number")
            @RequestParam("page") Optional<Integer> page,
            @ApiParam("Page size. Max size is 100, default size is 20")
            @RequestParam("pageSize") Optional<Integer> pageSize) {
        return blogBlockingService.findByBlog(
                id,
                page.orElse(0),
                pageSize.orElse(20)
        );
    }

    @ApiOperation(
            value = "Retrieves not ended blockings of blog with specified id",
            notes = "Requires current user to be either blog's owner " +
                    "or blog's manager"
    )
    @ApiImplicitParams({
            @ApiImplicitParam(
                    value = "Access token",
                    name = "Authorization",
                    paramType = "header",
                    required = true
            )
    })
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Successfully retrieved blog blockings of blog",
                    response = BlogBlockingDTO[].class
            ),
            @ApiResponse(code = 400, message = "Invalid request"),
            @ApiResponse(code = 401, message = "Access token has expired"),
            @ApiResponse(code = 403, message = "Current user can't do this operation"),
            @ApiResponse(code = 404, message = "Blog with specified id not found")
    })
    @PreAuthorize("hasRole('USER') and @blogBlockingPermissionResolver.canViewBlogBlockingsInBlog(#id)")
    @GetMapping("/{id}/blockings/not-ended")
    public List<BlogBlockingDTO> findNotEndedBlogBlockingsByBlog(
            @ApiParam(value = "Id of blog", required = true)
            @PathVariable("id") Long id,
            @ApiParam("Page number")
            @RequestParam("page") Optional<Integer> page,
            @ApiParam("Page size. Max size is 100, default size is 20")
            @RequestParam("pageSize") Optional<Integer> pageSize) {
        return blogBlockingService.findNotEndedByBlog(
                id,
                page.orElse(0),
                pageSize.orElse(20)
        );
    }

    @PreAuthorize("hasRole('USER') && @blogBlockingPermissionResolver.canViewBlogBlockingsInBlog(#id)")
    @GetMapping(value = "/{id}/blockings", params = {"blockedUserDisplayedUsername"})
    public List<BlogBlockingDTO> findBlogBlockingsByBlogAndBlockedUserDisplayedUsername(
            @PathVariable("id") Long id,
            @RequestParam("blockedUserDisplayedUsername") String blockedUserDisplayedUsername,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("pageSize") Optional<Integer> pageSize) {
        return blogBlockingService.findByBlogAndBlockedUserDisplayedUsernameContains(
                id,
                blockedUserDisplayedUsername,
                page.orElse(0),
                pageSize.orElse(20)
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
                id,
                blockedUserDisplayedUsername,
                page.orElse(0),
                pageSize.orElse(20)
        );
    }

    @ApiOperation("Retrieves minified blog with specified id")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Successfully retrieved minified blog",
                    response = BlogMinifiedDTO.class
            ),
            @ApiResponse(code = 400, message = "Invalid request"),
            @ApiResponse(code = 401, message = "Access token has expired"),
            @ApiResponse(code = 403, message = "Current user can't do this operation"),
            @ApiResponse(code = 404, message = "Blog with specified id not found")
    })
    @GetMapping("/{id}/minified")
    public BlogMinifiedDTO findMinifiedById(@PathVariable("id") Long id) {
        return blogService.findMinifiedById(id);
    }

    @ApiOperation("Retrieves pinned blog posts of blog with specified id")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Successfully retrieved pinned blog posts",
                    response = BlogPostDTO[].class
            )
    })
    @IncreaseNumberOfViews(For.MULTIPLE_BLOG_POSTS)
    @GetMapping("/{id}/blog-posts/pinned")
    public List<BlogPostDTO> findPinnedBlogPostsByBlog(@PathVariable("id") Long id) {
        return blogPostService.findPinnedByBlog(id);
    }
}