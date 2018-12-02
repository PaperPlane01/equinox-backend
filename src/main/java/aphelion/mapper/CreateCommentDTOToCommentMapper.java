package aphelion.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import aphelion.exception.BlogPostNotFoundException;
import aphelion.exception.CommentNotFoundException;
import aphelion.model.domain.Comment;
import aphelion.model.dto.CreateCommentDTO;
import aphelion.repository.BlogPostRepository;
import aphelion.repository.CommentRepository;
import aphelion.security.AuthenticationFacade;
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
        } else {
            comment.setReferredComment(null);
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
