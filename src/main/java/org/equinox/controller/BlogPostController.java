package org.equinox.controller;

import lombok.RequiredArgsConstructor;
import org.equinox.model.CommentsDisplayMode;
import org.equinox.model.dto.BlogPostDTO;
import org.equinox.model.dto.BlogPostLikeDTO;
import org.equinox.model.dto.CommentDTO;
import org.equinox.model.dto.CreateBlogPostDTO;
import org.equinox.model.dto.UpdateBlogPostDTO;
import org.equinox.service.BlogPostLikeService;
import org.equinox.service.BlogPostService;
import org.equinox.service.CommentService;
import org.equinox.annotation.IncreaseNumberOfViews;
import org.equinox.annotation.IncreaseNumberOfViews.For;
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

    @IncreaseNumberOfViews(For.MULTIPLE_BLOG_POSTS)
    @GetMapping(params = {"blogId"})
    public List<BlogPostDTO> findByBlog(@RequestParam("blogId") Long blogId,
                                        @RequestParam(value = "page") Optional<Integer> page,
                                        @RequestParam(value = "pageSize") Optional<Integer> pageSize,
                                        @RequestParam(value = "sortingDirection")
                                                    Optional<String> sortingDirection,
                                        @RequestParam(value = "sortBy") Optional<String> sortBy) {
        return blogPostService.findByBlog(blogId, page.orElse(0), pageSize.orElse(10),
                sortingDirection.orElse("asc"), sortBy.orElse("id"));
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
        if (page.isPresent()) {
            return commentService.findByBlogPost(blogPostId, page.get(),
                    pageSize.orElse(100), sortingDirection.orElse("asc"),
                    sortBy.orElse("id"), CommentsDisplayMode.fromString(commentsDisplayMode
                            .orElse("flat")));
        } else {
            return commentService.findAllByBlogPost(blogPostId, sortingDirection.orElse("asc"),
                    sortBy.orElse("id"), CommentsDisplayMode.fromString(commentsDisplayMode
                            .orElse("flat")));
        }
    }

    @PreAuthorize("hasRole('USER') && @blogPostPermissionResolver.canViewDeletedBlogPost(#id)")
    @GetMapping("/deleted/{id}")
    public BlogPostDTO findDeletedById(@PathVariable("id") Long id) {
        return blogPostService.findDeletedById(id);
    }
}