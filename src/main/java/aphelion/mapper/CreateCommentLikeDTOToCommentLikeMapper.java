package aphelion.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import aphelion.exception.EntityNotFoundExceptionFactory;
import aphelion.model.domain.Comment;
import aphelion.model.domain.CommentLike;
import aphelion.model.dto.CreateCommentLikeDTO;
import aphelion.repository.CommentRepository;
import aphelion.security.AuthenticationFacade;
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