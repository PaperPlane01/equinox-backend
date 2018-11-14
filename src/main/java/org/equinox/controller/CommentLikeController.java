package org.equinox.controller;

import lombok.RequiredArgsConstructor;
import org.equinox.model.dto.CreateCommentLikeDTO;
import org.equinox.model.dto.UpdatedNumberOfCommentLikesDTO;
import org.equinox.service.CommentLikeService;
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
