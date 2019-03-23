package aphelion.controller;

import aphelion.model.dto.BlogPostReportDTO;
import aphelion.model.dto.CreateBlogPostReportDTO;
import aphelion.model.dto.UpdateBlogPostReportDTO;
import lombok.RequiredArgsConstructor;
import aphelion.service.BlogPostReportService;
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
@RequestMapping("/api/blog-post-reports")
@RequiredArgsConstructor
public class BlogPostReportController {
    private final BlogPostReportService blogPostReportService;

    @PostMapping
    public BlogPostReportDTO save(@RequestBody @Valid CreateBlogPostReportDTO createBlogPostReportDTO) {
        return blogPostReportService.save(createBlogPostReportDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public BlogPostReportDTO update(@PathVariable("id") Long id,
                                    @RequestBody @Valid UpdateBlogPostReportDTO updateBlogPostReportDTO) {
        return blogPostReportService.update(id, updateBlogPostReportDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        blogPostReportService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public BlogPostReportDTO findById(@PathVariable("id") Long id) {
        return blogPostReportService.findById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<BlogPostReportDTO> findAll(@RequestParam("page") Optional<Integer> page,
                                           @RequestParam("pageSize") Optional<Integer> pageSize,
                                           @RequestParam("sortingDirection") Optional<String> sortingDirection,
                                           @RequestParam("sortBy") Optional<String> sortBy) {
        return blogPostReportService.findAll(page.orElse(0), pageSize.orElse(50),
                sortingDirection.orElse("desc"), sortBy.orElse("id"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(params = {"blogPostId"})
    public List<BlogPostReportDTO> findByBlogPost(@RequestParam("blogPostId") Long blogPostId,
                                                  @RequestParam("page") Optional<Integer> page,
                                                  @RequestParam("pageSize") Optional<Integer> pageSize,
                                                  @RequestParam("sortingDirection") Optional<String> sortingDirection,
                                                  @RequestParam("sortBy") Optional<String> sortBy) {
        return blogPostReportService.findByBlogPost(blogPostId, page.orElse(0), pageSize.orElse(50),
                sortingDirection.orElse("desc"), sortBy.orElse("id"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/multiple")
    public List<BlogPostReportDTO> updateMultiple(
            @RequestBody @Valid List<UpdateBlogPostReportDTO> updateBlogPostReportDTOList) {
        return blogPostReportService.updateMultiple(updateBlogPostReportDTOList);
    }
}
