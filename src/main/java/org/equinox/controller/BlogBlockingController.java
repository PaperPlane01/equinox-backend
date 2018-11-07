package org.equinox.controller;

import lombok.RequiredArgsConstructor;
import org.equinox.model.dto.BlogBlockingDTO;
import org.equinox.model.dto.CreateBlogBlockingDTO;
import org.equinox.model.dto.UpdateBlogBlockingDTO;
import org.equinox.service.BlogBlockingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/blog-blockings")
@RequiredArgsConstructor
public class BlogBlockingController {
    private final BlogBlockingService blogBlockingService;

    @PreAuthorize("hasRole('USER') and " +
            "@blogBlockingPermissionResolver.canBlockUsersInBlog(#createBlogBlockingDTO.blogId)")
    @PostMapping
    public BlogBlockingDTO save(@RequestBody @Valid CreateBlogBlockingDTO createBlogBlockingDTO) {
        return blogBlockingService.save(createBlogBlockingDTO);
    }

    @PreAuthorize("hasRole('USER') and @blogBlockingPermissionResolver.canUpdateBlocking(#id)")
    @PutMapping("/{id}")
    public BlogBlockingDTO update(@PathVariable("id") Long id,
                                  @RequestBody @Valid UpdateBlogBlockingDTO updateBlogBlockingDTO) {
        return blogBlockingService.update(id, updateBlogBlockingDTO);
    }

    @PreAuthorize("hasRole('USER') and @blogBlockingPermissionResolver.canUnblockUser(#id)")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        blogBlockingService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
