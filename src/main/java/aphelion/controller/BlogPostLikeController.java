package aphelion.controller;

import aphelion.model.dto.CreateBlogPostLikeDTO;
import aphelion.model.dto.UpdatedNumberOfBlogPostLikesDTO;
import lombok.RequiredArgsConstructor;
import aphelion.service.BlogPostLikeService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/blog-post-likes")
@RequiredArgsConstructor
public class BlogPostLikeController {
    private final BlogPostLikeService blogPostLikeService;

    @PreAuthorize("hasRole('USER') " +
            "and @blogPostLikePermissionResolver.canSaveBlogPostLike(#createBlogPostLikeDTO.blogPostId)")
    @PostMapping
    public UpdatedNumberOfBlogPostLikesDTO save(@RequestBody @Valid CreateBlogPostLikeDTO createBlogPostLikeDTO) {
        return blogPostLikeService.save(createBlogPostLikeDTO);
    }

    @PreAuthorize("hasRole('USER') and @blogPostLikePermissionResolver.canDeleteBlogPostLike(#id)")
    @DeleteMapping("/{id}")
    public UpdatedNumberOfBlogPostLikesDTO delete(@PathVariable("id") Long id) {
        return blogPostLikeService.delete(id);
    }
}