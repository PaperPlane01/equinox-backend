package aphelion.controller;

import aphelion.model.dto.CommentReportDTO;
import aphelion.model.dto.CreateCommentReportDTO;
import aphelion.model.dto.UpdateCommentReportDTO;
import lombok.RequiredArgsConstructor;
import aphelion.service.CommentReportService;
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
@RequestMapping("/api/comment-reports")
@RequiredArgsConstructor
public class CommentReportController {
    private final CommentReportService commentReportService;

    @PostMapping
    public CommentReportDTO save(@RequestBody @Valid CreateCommentReportDTO createCommentReportDTO) {
        return commentReportService.save(createCommentReportDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public CommentReportDTO update(@PathVariable("id") Long id,
                                   @RequestBody @Valid UpdateCommentReportDTO updateCommentReportDTO) {
        return commentReportService.update(id, updateCommentReportDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/multiple")
    public List<CommentReportDTO> updateMultiple(
            @RequestBody @Valid List<UpdateCommentReportDTO> updateCommentReportDTOs) {
        return commentReportService.updateMultiple(updateCommentReportDTOs);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public CommentReportDTO findById(@PathVariable("id") Long id) {
        return commentReportService.findById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        commentReportService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<CommentReportDTO> findAll(@RequestParam(value = "page") Optional<Integer> page,
                                          @RequestParam(value = "pageSize") Optional<Integer> pageSize,
                                          @RequestParam(value = "sortingDirection") Optional<String> sortingDirection,
                                          @RequestParam(value = "sortBy") Optional<String> sortBy) {
        return commentReportService.findAll(page.orElse(0), pageSize.orElse(50),
                sortingDirection.orElse("desc"), sortBy.orElse("id"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(params = {"commentId"})
    public List<CommentReportDTO> findByComment(@RequestParam("commentId") Long commentId,
                                                @RequestParam("page") Optional<Integer> page,
                                                @RequestParam("pageSize") Optional<Integer> pageSize,
                                                @RequestParam("sortingDirection") Optional<String> sortingDirection,
                                                @RequestParam("sortBy") Optional<String> sortBy) {
        return commentReportService.findByComment(commentId, page.orElse(0), pageSize.orElse(50),
                sortingDirection.orElse("desc"), sortBy.orElse("id"));
    }
}