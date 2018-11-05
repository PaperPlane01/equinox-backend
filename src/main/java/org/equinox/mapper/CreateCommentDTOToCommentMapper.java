package org.equinox.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.equinox.exception.BlogPostNotFoundException;
import org.equinox.exception.CommentNotFoundException;
import org.equinox.model.domain.Comment;
import org.equinox.model.dto.CreateCommentDTO;
import org.equinox.repository.BlogPostRepository;
import org.equinox.repository.CommentRepository;
import org.equinox.security.AuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;

@Mapper
public abstract class CreateCommentDTOToCommentMapper {
    @Autowired
    private BlogPostRepository blogPostRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @BeforeMapping
    protected void setFields(CreateCommentDTO createCommentDTO,
                             @MappingTarget Comment comment) {
        comment.setLikes(Collections.emptyList());
        comment.setCreatedAt(Date.from(Instant.now()));
        comment.setDeleted(false);
        comment.setBlogPost(blogPostRepository
                .findById(createCommentDTO.getBlogPostId())
                .orElseThrow(() -> new BlogPostNotFoundException("Could not find blog post " +
                        "with given id " + createCommentDTO.getBlogPostId())));
        if (createCommentDTO.getRootCommentId() != null) {
            comment.setRootComment(findCommentById(createCommentDTO.getRootCommentId()));
            comment.setRoot(false);
        } else {
            comment.setRoot(true);
        }

        if (createCommentDTO.getReferredCommentId() != null) {
            comment.setReferredComment(findCommentById(createCommentDTO.getReferredCommentId()));
        }

        comment.setAuthor(authenticationFacade.getCurrentUser());
    }

    private Comment findCommentById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Could not find comment with given id " + id));
    }

    @BeanMapping(resultType = Comment.class)
    public abstract Comment map(CreateCommentDTO createCommentDTO);
}
