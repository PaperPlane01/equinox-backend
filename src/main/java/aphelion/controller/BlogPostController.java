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
import aphelion.service.BlogPostService;
import lombok.RequiredArgsConstructor;
import aphelion.service.BlogPostLikeService;
import aphelion.service.CommentService;
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
public class BlogPostController {
    private final BlogPostService blogPostService;
    private final BlogPostLikeService blogPostLikeService;
    private final CommentService commentService;

    @IncreaseNumberOfViews(For.SINGLE_BLOG_POST)
    @GetMapping("/{id}")
    public BlogPostDTO findById(@PathVariable("id") Long id) {
       return blogPostService.findById(id);
    }

    @PreAuthorize("hasRole('USER') and @blogPostPermissionResolver.canSaveBlogPost(#createBlogPostDTO.blogId)")
    @PostMapping
    public BlogPostDTO save(@RequestBody @Valid CreateBlogPostDTO createBlogPostDTO) {
       return blogPostService.save(createBlogPostDTO);
    }

    @PreAuthorize("hasRole('USER') and @blogPostPermissionResolver.canUpdateBlogPost(#id)")
    @PutMapping("/{id}")
    public BlogPostDTO update(@PathVariable("id") Long id,
                              @RequestBody @Valid UpdateBlogPostDTO updateBlogPostDTO) {
        return blogPostService.update(id, updateBlogPostDTO);
    }

    @PreAuthorize("(hasRole('USER') and @blogPostPermissionResolver.canDeleteBlogPost(#id))")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        blogPostService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{blogPostId}/likes")
    public List<BlogPostLikeDTO> findLikesByBlogPost(@PathVariable("blogPostId") Long blogPostId,
                                                     @RequestParam(value = "page")
                                                             Optional<Integer> page,
                                                     @RequestParam(value = "pageSize")
                                                                 Optional<Integer> pageSize,
                                                     @RequestParam(value = "sortingDirection")
                                                                 Optional<String> sortingDirection,
                                                     @RequestParam(value = "sortBy")
                                                                 Optional<String> sortBy) {
        return blogPostLikeService.findByBlogPost(blogPostId, page.orElse(0),
                    pageSize.orElse(50), sortingDirection.orElse("desc"),
                sortBy.orElse("id"));
    }

    @GetMapping("/{blogPostId}/comments")
    public List<CommentDTO> findCommentsOfBlogPost(@PathVariable("blogPostId") Long blogPostId,
                                                   @RequestParam("page") Optional<Integer> page,
                                                   @RequestParam("pageSize") Optional<Integer> pageSize,
                                                   @RequestParam("sortingDirection") Optional<String>
                                                               sortingDirection,
                                                   @RequestParam("sortBy") Optional<String> sortBy,
                                                   @RequestParam("commentsDisplayMode") Optional<String>
                                                               commentsDisplayMode) {

        return commentService.findByBlogPost(blogPostId, page.orElse(0),
                pageSize.orElse(100), sortingDirection.orElse("asc"),
                sortBy.orElse("id"), CommentsDisplayMode.fromString(commentsDisplayMode
                        .orElse("flat")));
    }

    @PreAuthorize("hasRole('USER') && @blogPostPermissionResolver.canViewDeletedBlogPost(#id)")
    @GetMapping("/deleted/{id}")
    public BlogPostDTO findDeletedById(@PathVariable("id") Long id) {
        return blogPostService.findDeletedById(id);
    }

    @PreAuthorize("@blogPostPermissionResolver.canSeeBlogPostAuthor(#id)")
    @GetMapping("/{id}/author")
    public UserDTO findBlogPostAuthor(@PathVariable("id") Long id) {
        return blogPostService.findAuthorOfBlogPost(id);
    }

    @PreAuthorize("hasRole('USER')")
    @IncreaseNumberOfViews(For.MULTIPLE_BLOG_POSTS)
    @GetMapping("/feed")
    public List<BlogPostDTO> getFeed(@RequestParam("page") Optional<Integer> page,
                                     @RequestParam("pageSize") Optional<Integer> pageSize) {
        return blogPostService.getFeed(page.orElse(0), pageSize.orElse(20));
    }

    @IncreaseNumberOfViews(For.MULTIPLE_BLOG_POSTS)
    @GetMapping("/most-popular/for-week")
    public List<BlogPostDTO> getMostPopularForWeek(
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("pageSize") Optional<Integer> pageSize
    ) {
        return blogPostService.getMostPopularForWeek(page.orElse(0),
                pageSize.orElse(10));
    }

    @IncreaseNumberOfViews(For.MULTIPLE_BLOG_POSTS)
    @GetMapping("/most-popular/for-month")
    public List<BlogPostDTO> getMostPopularForMonth(
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("pageSize") Optional<Integer> pageSize
    ) {
        return blogPostService.getMostPopularForMonth(page.orElse(0),
                pageSize.orElse(10));
    }

    @IncreaseNumberOfViews(For.MULTIPLE_BLOG_POSTS)
    @GetMapping("/most-popular/for-year")
    public List<BlogPostDTO> getMostPopularForYear(
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("pageSize") Optional<Integer> pageSize
    ) {
        return blogPostService.getMostPopularForYear(page.orElse(0),
                pageSize.orElse(10));
    }

    @IncreaseNumberOfViews(For.MULTIPLE_BLOG_POSTS)
    @GetMapping(value = "/most-popular/for-period", params = {"from", "to"})
    public List<BlogPostDTO> getMostPopularForPeriod(
            @RequestParam("from") @DateTimeFormat(pattern = "dd-MM-yyyy") Date from,
            @RequestParam("to") @DateTimeFormat(pattern = "dd-MM-yyyy") Date to,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("pageSize") Optional<Integer> pageSize
    ) {
        return blogPostService.getMostPopularForPeriod(from, to,
                page.orElse(0), pageSize.orElse(10));
    }

    @PreAuthorize("hasRole('USER') && @blogPostPermissionResolver.canPinBlogPost(#id)")
    @PostMapping("/{id}/pin")
    public BlogPostDTO pin(@PathVariable("id") Long id) {
        return blogPostService.pin(id);
    }

    @PreAuthorize("hasRole('USER') && @blogPostPermissionResolver.canUnpinBlogPost(#id)")
    @DeleteMapping("/{id}/unpin")
    public BlogPostDTO unpin(@PathVariable("id") Long id) {
        return blogPostService.unpin(id);
    }
}