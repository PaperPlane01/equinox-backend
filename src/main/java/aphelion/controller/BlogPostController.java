package aphelion.controller;

import aphelion.annotation.IncreaseNumberOfViews;
import aphelion.annotation.IncreaseNumberOfViews.For;
import aphelion.model.CommentsDisplayMode;
import aphelion.model.dto.BlogPostDTO;
import aphelion.model.dto.BlogPostLikeDTO;
import aphelion.model.dto.CommentDTO;
import aphelion.model.dto.CreateBlogPostDTO;
import aphelion.model.dto.UpdateBlogPostDTO;
import aphelion.model.dto.UserDTO;
import aphelion.service.BlogPostLikeService;
import aphelion.service.BlogPostService;
import aphelion.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/blog-posts")
@RequiredArgsConstructor
@Api(value = "Blog post controller", description = "Operations over blog posts")
public class BlogPostController {
    private final BlogPostService blogPostService;
    private final BlogPostLikeService blogPostLikeService;
    private final CommentService commentService;

    @ApiOperation("Retrieves blog post with specified id")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Successfully retrieved blog post with specified id",
                    response = BlogPostDTO.class
            ),
            @ApiResponse(code = 400, message = "Invalid request"),
            @ApiResponse(code = 401, message = "Access token has expired"),
            @ApiResponse(code = 404, message = "Blog post with specified id not found")
    })
    @IncreaseNumberOfViews(For.SINGLE_BLOG_POST)
    @GetMapping("/{id}")
    public BlogPostDTO findById(
            @ApiParam(value = "Id of blog post", required = true)
            @PathVariable("id") Long id) {
        return blogPostService.findById(id);
    }

    @ApiOperation(
            value = "Creates new blog post",
            notes = "Requires current user to be either blog's owner " +
                    "or blog's manager with <code>EDITOR</code> role"
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
                    code = 201,
                    message = "Successfully created new blog post",
                    response = BlogPostDTO.class
            ),
            @ApiResponse(code = 400, message = "Invalid request"),
            @ApiResponse(code = 401, message = "Access token has expired"),
            @ApiResponse(code = 403, message = "Current user can't do this operation"),
            @ApiResponse(code = 404, message = "Blog with specified id not found")
    })
    @PreAuthorize("hasRole('USER') and @blogPostPermissionResolver.canSaveBlogPost(#createBlogPostDTO.blogId)")
    @PostMapping
    public BlogPostDTO save(
            @ApiParam(
                    name = "Create blog post request",
                    required = true
            )
            @RequestBody @Valid CreateBlogPostDTO createBlogPostDTO) {
       return blogPostService.save(createBlogPostDTO);
    }

    @ApiOperation(
            value = "Updates blog post with specified id",
            notes = "Requires current user to be either blog's owner " +
                    "or blog's manager with <code>EDITOR</code> role and the same user " +
                    "who created this blog post"
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
                    message = "Successfully updated blog post",
                    response = BlogPostDTO.class
            ),
            @ApiResponse(code = 400, message = "Invalid request"),
            @ApiResponse(code = 401, message = "Access token has expired"),
            @ApiResponse(code = 403, message = "Current user can't do this operation"),
            @ApiResponse(code = 404, message = "Blog with specified id not found")
    })
    @PreAuthorize("hasRole('USER') and @blogPostPermissionResolver.canUpdateBlogPost(#id)")
    @PutMapping("/{id}")
    public BlogPostDTO update(
            @ApiParam(value = "Id of blog", required = true)
            @PathVariable("id") Long id,
            @ApiParam(
                    name = "Update blog post request",
                    value = "Object representing updated blog post"
            )
            @RequestBody @Valid UpdateBlogPostDTO updateBlogPostDTO) {
        return blogPostService.update(id, updateBlogPostDTO);
    }

    @ApiOperation(
            value = "Deletes blog post with specified id",
            notes = "Requires current user to be either blog's owner " +
                    "or blog's manager with <code>EDITOR</code> role and the same user " +
                    "who created this blog post"
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
                    code = 204,
                    message = "Successfully deleted blog post"
            ),
            @ApiResponse(code = 400, message = "Invalid request"),
            @ApiResponse(code = 401, message = "Access token has expired"),
            @ApiResponse(code = 403, message = "Current user can't do this operation"),
            @ApiResponse(code = 404, message = "Blog with specified id not found")
    })
    @PreAuthorize("(hasRole('USER') and @blogPostPermissionResolver.canDeleteBlogPost(#id))")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @ApiParam(value = "Id of blog post to be deleted", required = true)
            @PathVariable("id") Long id) {
        blogPostService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation("Retrieves likes of blog post with specified id")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Successfully retrieved likes of blog post",
                    response = BlogPostLikeDTO[].class
            ),
            @ApiResponse(code = 400, message = "Invalid request"),
            @ApiResponse(code = 401, message = "Access token expired"),
            @ApiResponse(code = 404, message = "Blog post with specified id not found")
    })
    @GetMapping("/{blogPostId}/likes")
    public List<BlogPostLikeDTO> findLikesByBlogPost(
            @ApiParam(value = "Id of blog post", required = true)
            @PathVariable("blogPostId") Long blogPostId,
            @ApiParam(value = "Page number", type = "int")
            @RequestParam("page") Optional<Integer> page,
            @ApiParam(value = "Page size. Max size is 200", type = "int")
            @RequestParam("pageSize") Optional<Integer> pageSize,
            @ApiParam(value = "Sorting direction. Allowed values are asc, desc")
            @RequestParam("sortingDirection") Optional<String> sortingDirection,
            @ApiParam(value = "Sorting property. Allowed properties are <code>id</code>")
            @RequestParam("sortBy") Optional<String> sortBy) {
        return blogPostLikeService.findByBlogPost(
                blogPostId,
                page.orElse(0),
                pageSize.orElse(50),
                sortingDirection.orElse("desc"),
                sortBy.orElse("id")
        );
    }

    @ApiOperation("Retrieves comments of blog post with specified id")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Successfully retrieved comments of blog post",
                    response = CommentDTO[].class
            ),
            @ApiResponse(code = 400, message = "Invalid request"),
            @ApiResponse(code = 401, message = "Access token expired"),
            @ApiResponse(code = 404, message = "Blog post with specified id not found")
    })
    @GetMapping("/{blogPostId}/comments")
    public List<CommentDTO> findCommentsOfBlogPost(
            @ApiParam(value = "Id of blog post", required = true)
            @PathVariable("blogPostId") Long blogPostId,
            @ApiParam(
                    value = "Page number",
                    type = "integer",
                    defaultValue = "0"
            )
            @RequestParam("page") Optional<Integer> page,
            @ApiParam(
                    value = "Page size. Max size is 200",
                    type = "integer",
                    defaultValue = "100"
            )
            @RequestParam("pageSize") Optional<Integer> pageSize,
            @ApiParam(
                    value = "Sorting direction",
                    allowableValues = "asc, desc",
                    defaultValue = "asc"
            )
            @RequestParam("sortingDirection") Optional<String> sortingDirection,
            @ApiParam(
                    value = "Sorting property",
                    allowableValues = "id, createdAt, numberOfLikes",
                    defaultValue = "id"
            )
            @RequestParam("sortBy") Optional<String> sortBy,
            @ApiParam(
                    value = "Comments display mode",
                    allowableValues = "FLAT, ROOT_COMMENT",
                    defaultValue = "FLAT"
            )
            @RequestParam("commentsDisplayMode") Optional<String> commentsDisplayMode) {

        return commentService.findByBlogPost(
                blogPostId,
                page.orElse(0),
                pageSize.orElse(100),
                sortingDirection.orElse("asc"),
                sortBy.orElse("id"),
                CommentsDisplayMode.fromString(commentsDisplayMode
                        .orElse("flat")
                )
        );
    }
    @PreAuthorize("hasRole('USER') && @blogPostPermissionResolver.canViewDeletedBlogPost(#id)")
    @GetMapping("/deleted/{id}")
    public BlogPostDTO findDeletedById(@PathVariable("id") Long id) {
        return blogPostService.findDeletedById(id);
    }

    @ApiOperation(
            value = "Retrieves author of blog post with specified id",
            notes = "May require access token, depending on <code>blogManagersVisibilityLevel<code> " +
                    "property of blog"
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
                    message = "Successfully retrieved author of blog post",
                    response = UserDTO.class
            ),
            @ApiResponse(code = 400, message = "Invalid request"),
            @ApiResponse(code = 401, message = "Access token has expired"),
            @ApiResponse(code = 403, message = "Current user can't do this operation"),
            @ApiResponse(code = 404, message = "Blog post with specified id not found")
    })
    @PreAuthorize("@blogPostPermissionResolver.canSeeBlogPostAuthor(#id)")
    @GetMapping("/{id}/author")
    public UserDTO findBlogPostAuthor(@PathVariable("id") Long id) {
        return blogPostService.findAuthorOfBlogPost(id);
    }

    @ApiOperation(
            value = "Retrieves feed of current user",
            notes = "Requires current user to be authenticated"
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
                    message = "Successfully retrieved feed of current user",
                    response = BlogPostDTO[].class
            ),
            @ApiResponse(code = 400, message = "Invalid request"),
            @ApiResponse(code = 401, message = "Access token has expired"),
            @ApiResponse(code = 403, message = "Current user can't do this operation"),
    })
    @PreAuthorize("hasRole('USER')")
    @IncreaseNumberOfViews(For.MULTIPLE_BLOG_POSTS)
    @GetMapping("/feed")
    public List<BlogPostDTO> getFeed(
            @ApiParam(
                    value = "Page number",
                    defaultValue = "0"
            )
            @RequestParam("page") Optional<Integer> page,
            @ApiParam(
                    value = "Page size. Max size is 50",
                    defaultValue = "20"
            )
            @RequestParam("pageSize") Optional<Integer> pageSize) {
        return blogPostService.getFeed(page.orElse(0), pageSize.orElse(20));
    }

    @ApiOperation("Retrieves most popular blog posts for week")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Successfully retrieved most popular blog posts",
                    response = BlogPostDTO[].class
            ),
            @ApiResponse(code = 400, message = "Invalid request"),
            @ApiResponse(code = 401, message = "Access token has expired"),
    })
    @IncreaseNumberOfViews(For.MULTIPLE_BLOG_POSTS)
    @GetMapping("/most-popular/for-week")
    public List<BlogPostDTO> getMostPopularForWeek(
            @ApiParam(value = "Page number", defaultValue = "0")
            @RequestParam("page") Optional<Integer> page,
            @ApiParam(value = "Page size. Max size is 50", defaultValue = "10")
            @RequestParam("pageSize") Optional<Integer> pageSize) {
        return blogPostService.getMostPopularForWeek(
                page.orElse(0),
                pageSize.orElse(10)
        );
    }

    @ApiOperation("Retrieves most popular blog posts for month")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Successfully retrieved most popular blog posts",
                    response = BlogPostDTO[].class
            ),
            @ApiResponse(code = 400, message = "Invalid request"),
            @ApiResponse(code = 401, message = "Access token has expired"),
    })
    @IncreaseNumberOfViews(For.MULTIPLE_BLOG_POSTS)
    @GetMapping("/most-popular/for-month")
    public List<BlogPostDTO> getMostPopularForMonth(
            @ApiParam(value = "Page number", defaultValue = "0")
            @RequestParam("page") Optional<Integer> page,
            @ApiParam(value = "Page size. Max size is 50", defaultValue = "10")
            @RequestParam("pageSize") Optional<Integer> pageSize) {
        return blogPostService.getMostPopularForMonth(
                page.orElse(0),
                pageSize.orElse(10)
        );
    }

    @ApiOperation("Retrieves most popular blog posts for year")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Successfully retrieved most popular blog posts",
                    response = BlogPostDTO[].class
            ),
            @ApiResponse(code = 400, message = "Invalid request"),
            @ApiResponse(code = 401, message = "Access token has expired"),
    })
    @IncreaseNumberOfViews(For.MULTIPLE_BLOG_POSTS)
    @GetMapping("/most-popular/for-year")
    public List<BlogPostDTO> getMostPopularForYear(
            @ApiParam(value = "Page number", defaultValue = "0")
            @RequestParam("page") Optional<Integer> page,
            @ApiParam(value = "Page size. Max size is 50", defaultValue = "10")
            @RequestParam("pageSize") Optional<Integer> pageSize) {
        return blogPostService.getMostPopularForYear(
                page.orElse(0),
                pageSize.orElse(10)
        );
    }

    @ApiOperation("Retrieves most popular blog posts for period")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Successfully retrieved most popular blog posts",
                    response = BlogPostDTO[].class
            ),
            @ApiResponse(code = 400, message = "Invalid request"),
            @ApiResponse(code = 401, message = "Access token has expired"),
    })
    @IncreaseNumberOfViews(For.MULTIPLE_BLOG_POSTS)
    @GetMapping(value = "/most-popular/for-period", params = {"from", "to"})
    public List<BlogPostDTO> getMostPopularForPeriod(
            @ApiParam(value = "Start of the period", required = true, format = "dd-MM-yyyy")
            @RequestParam("from") @DateTimeFormat(pattern = "dd-MM-yyyy") Date from,
            @ApiParam(value = "End of the period", required = true, format = "dd-MM-yyyy")
            @RequestParam("to") @DateTimeFormat(pattern = "dd-MM-yyyy") Date to,
            @ApiParam(value = "Page number", defaultValue = "0")
            @RequestParam("page") Optional<Integer> page,
            @ApiParam(value = "Page size. Max size is 50", defaultValue = "10")
            @RequestParam("pageSize") Optional<Integer> pageSize) {
        return blogPostService.getMostPopularForPeriod(
                from,
                to,
                page.orElse(0),
                pageSize.orElse(10)
        );
    }

    @ApiOperation(
            value = "Pins blog post with specified id",
            notes = "Current user must be an owner of the blog"
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
                    message = "Successfully pinned blog lost",
                    response = BlogPostDTO.class
            ),
            @ApiResponse(code = 400, message = "Invalid request"),
            @ApiResponse(code = 401, message = "Access token has expired"),
            @ApiResponse(code = 403, message = "Current user can't do this operation")
    })
    @PreAuthorize("hasRole('USER') && @blogPostPermissionResolver.canPinBlogPost(#id)")
    @PostMapping("/{id}/pin")
    public BlogPostDTO pin(
            @ApiParam(value = "Id of blog post to be pinned", required = true)
            @PathVariable("id") Long id) {
        return blogPostService.pin(id);
    }

    @ApiOperation(
            value = "Unpins blog post with specified id",
            notes = "Current user must be an owner of the blog"
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
                    message = "Successfully unpinned blog lost",
                    response = BlogPostDTO.class
            ),
            @ApiResponse(code = 400, message = "Invalid request"),
            @ApiResponse(code = 401, message = "Access token has expired"),
            @ApiResponse(code = 403, message = "Current user can't do this operation")
    })
    @PreAuthorize("hasRole('USER') && @blogPostPermissionResolver.canUnpinBlogPost(#id)")
    @DeleteMapping("/{id}/unpin")
    public BlogPostDTO unpin(
            @ApiParam(value = "Id of blog post to be unpinned", required = true)
            @PathVariable("id") Long id) {
        return blogPostService.unpin(id);
    }

    @ApiOperation("Searches blog posts")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Successfully searched blog posts",
                    response = BlogPostDTO[].class
            ),
            @ApiResponse(code = 400, message = "Invalid request"),
            @ApiResponse(code = 401, message = "Access token has expired"),
    })
    @IncreaseNumberOfViews(For.MULTIPLE_BLOG_POSTS)
    @GetMapping(value = "/search", params = {"query"})
    public List<BlogPostDTO> search(
            @ApiParam(value = "Query to search", required = true)
            @RequestParam("query") String query,
            @ApiParam(value = "Page number", defaultValue = "0")
            @RequestParam("page") Optional<Integer> page,
            @ApiParam(value = "Page size. Max size is 50", defaultValue = "10")
            @RequestParam("pageSize") Optional<Integer> pageSize,
            @ApiParam(value = "Direction of sorting", allowableValues = "asc, desc", defaultValue = "desc")
            @RequestParam("sortingDirection") Optional<String> sortingDirection,
            @ApiParam(
                    value = "Sorting property",
                    allowableValues = "id, creationDate, popularity",
                    defaultValue = "popularity"
            )
            @RequestParam("sortBy") Optional<String> sortBy) {
        return blogPostService.search(
                query,
                page.orElse(0),
                pageSize.orElse(10),
                sortingDirection.orElse("desc"),
                sortBy.orElse("popularity")
        );
    }

    @ApiOperation(
            value = "Deletes multiple blog posts with specified ids",
            notes = "Requires current user to have <code>ROLE_ADMIN</code> role"
    )
    @ApiResponses({
            @ApiResponse(code = 204, message = "Successfully deleted blog posts"),
            @ApiResponse(code = 400, message = "Invalid request"),
            @ApiResponse(code = 401, message = "Access token has expired"),
            @ApiResponse(code = 403, message = "Current user can't do this operation")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(
                    value = "Access token",
                    name = "Authorization",
                    paramType = "header",
                    required = true
            )
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/multiple")
    public ResponseEntity<?> deleteMultiple(
            @ApiParam(value = "List of ids of blog posts to be deleted", required = true)
            @RequestBody List<Long> ids) {
        blogPostService.deleteMultiple(ids);
        return ResponseEntity.noContent().build();
    }
}