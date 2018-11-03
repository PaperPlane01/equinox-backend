package org.equinox.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.equinox.exception.EntityNotFoundExceptionFactory;
import org.equinox.model.domain.Comment;
import org.equinox.model.domain.CommentLike;
import org.equinox.model.dto.CreateCommentLikeDTO;
import org.equinox.repository.CommentRepository;
import org.equinox.security.AuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper
public abstract class CreateCommentLikeDTOToCommentLikeMapper {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Autowired
    private EntityNotFoundExceptionFactory entityNotFoundExceptionFactory;

    @BeanMapping(resultType = CommentLike.class)
    public abstract CommentLike map(CreateCommentLikeDTO createCommentLikeDTO);

    @BeforeMapping
    protected void setFields(CreateCommentLikeDTO createCommentLikeDTO,
                             @MappingTarget CommentLike commentLike) {
        commentLike.setComment(commentRepository
                .findById(createCommentLikeDTO.getCommentId())
                .orElseThrow(() -> entityNotFoundExceptionFactory.create(Comment.class, createCommentLikeDTO.getCommentId())));
        commentLike.setUser(authenticationFacade.getCurrentUser());
    }
}