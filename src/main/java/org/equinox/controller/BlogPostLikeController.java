package org.equinox.controller;

import lombok.RequiredArgsConstructor;
import org.equinox.model.dto.CreateBlogPostLikeDTO;
import org.equinox.model.dto.UpdatedNumberOfBlogPostLikesDTO;
import org.equinox.service.BlogPostLikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<UpdatedNumberOfBlogPostLikesDTO> delete(@PathVariable("id") Long id) {
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(blogPostLikeService.delete(id));
    }
}
