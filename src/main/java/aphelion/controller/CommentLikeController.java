package aphelion.controller;

import aphelion.model.dto.CreateCommentLikeDTO;
import aphelion.model.dto.UpdatedNumberOfCommentLikesDTO;
import aphelion.service.CommentLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/comment-likes")
@RequiredArgsConstructor
public class CommentLikeController {
    private final CommentLikeService commentLikeService;

    @PreAuthorize("hasRole('USER') and @commentLikePermissionResolver.canSaveCommentLike(#createCommentLikeDTO.commentId)")
    @PostMapping
    public UpdatedNumberOfCommentLikesDTO save(@RequestBody @Valid CreateCommentLikeDTO createCommentLikeDTO) {
        return commentLikeService.save(createCommentLikeDTO);
    }

    @PreAuthorize("hasRole('USER') and @commentLikePermissionResolver.canDeleteCommentLike(#id)")
    @DeleteMapping("/{id}")
    public UpdatedNumberOfCommentLikesDTO delete(@PathVariable("id") Long id) {
        return commentLikeService.delete(id);
    }
}
