package aphelion.controller;

import aphelion.model.dto.CommentDTO;
import aphelion.model.dto.CommentLikeDTO;
import aphelion.model.dto.CommentReportDTO;
import aphelion.model.dto.CreateCommentDTO;
import aphelion.model.dto.RestoreOrDeleteCommentDTO;
import aphelion.model.dto.UpdateCommentDTO;
import lombok.RequiredArgsConstructor;
import aphelion.service.CommentLikeService;
import aphelion.service.CommentReportService;
import aphelion.service.CommentService;
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
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final CommentLikeService commentLikeService;
    private final CommentReportService commentReportService;

    @GetMapping("/{id}")
    public CommentDTO findById(@PathVariable("id") Long id) {
       return commentService.findById(id);
    }

    @PreAuthorize("hasRole('USER') and @commentPermissionResolver.canSaveComment(#createCommentDTO.blogPostId)")
    @PostMapping
    public CommentDTO save(@RequestBody CreateCommentDTO createCommentDTO) {
        return commentService.save(createCommentDTO);
    }

    @PreAuthorize("hasRole('USER') and @commentPermissionResolver.canUpdateComment(#id)")
    @PutMapping("/{id}")
    public CommentDTO update(@PathVariable("id") Long id,
                             @RequestBody UpdateCommentDTO updateCommentDTO) {
        return commentService.update(id, updateCommentDTO);
    }

    @PreAuthorize("(hasRole('USER') and @commentPermissionResolver.canDeleteComment(#id))")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        commentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{commentId}/likes")
    public List<CommentLikeDTO> findLikesByComment(@PathVariable("commentId") Long commentId,
                                                   @RequestParam(value = "page", required = false)
                                                           Optional<Integer> page,
                                                   @RequestParam(value = "pageSize", required = false)
                                                               Optional<Integer> pageSize,
                                                   @RequestParam(value = "sortingDirection", required = false)
                                                               Optional<String> sortingDirection,
                                                   @RequestParam(value = "sorBy", required = false)
                                                               Optional<String> sortBy) {
        return commentLikeService.findByComment(commentId, page.orElse(0), pageSize.orElse(50),
                    sortingDirection.orElse("desc"), sortBy.orElse("id"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{commentId}/reports")
    public List<CommentReportDTO> findReportsByComment(@PathVariable("commentId") Long commentId,
                                                       @RequestParam("page")
                                                               Optional<Integer> page,
                                                       @RequestParam("pageSize")
                                                                   Optional<Integer> pageSize,
                                                       @RequestParam("sortingDirection")
                                                                   Optional<String> sortingDirection,
                                                       @RequestParam("sortBy")
                                                                   Optional<String> sortBy) {
        return commentReportService.findByComment(commentId, page.orElse(0),
                pageSize.orElse(50), sortingDirection.orElse("desc"),
                sortBy.orElse("id"));
    }

    @GetMapping("/{commentId}/thread")
    public List<CommentDTO> findRepliesByComment(@PathVariable("commentId") Long commentId,
                                            @RequestParam("page") Optional<Integer> page,
                                            @RequestParam("pageSize") Optional<Integer> pageSize,
                                            @RequestParam("sortingDirection") Optional<String>
                                                             sortingDirection,
                                            @RequestParam("sortBy") Optional<String> sortBy) {
        return commentService.findByRootComment(commentId, page.orElse(0), pageSize.orElse(50),
                sortingDirection.orElse("asc"), sortBy.orElse("id"));
    }

    @PreAuthorize("hasRole('USER') " +
            "&& @commentPermissionResolver.canRestoreOrDeleteComment(#id, #restoreOrDeleteCommentDTO)")
    @PatchMapping("/{id}")
    public Object restore(@PathVariable("id") Long id,
                          @RequestBody @Valid RestoreOrDeleteCommentDTO restoreOrDeleteCommentDTO) {
        if (restoreOrDeleteCommentDTO.getDeleted()) {
            commentService.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            return commentService.restore(id);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/multiple")
    public ResponseEntity<?> deleteMultiple(@RequestBody List<Long> ids) {
        commentService.deleteMultiple(ids);
        return ResponseEntity.noContent().build();
    }
}