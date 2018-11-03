package org.equinox.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.equinox.exception.BlogPostNotFoundException;
import org.equinox.model.domain.BlogPostLike;
import org.equinox.model.dto.CreateBlogPostLikeDTO;
import org.equinox.repository.BlogPostRepository;
import org.equinox.security.AuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper
public abstract class CreateBlogPostLikeDTOToBlogPostLikeMapper {
    @Autowired
    private BlogPostRepository blogPostRepository;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @BeanMapping(resultType = BlogPostLike.class)
    public abstract BlogPostLike map(CreateBlogPostLikeDTO createBlogPostLikeDTO);

    @BeforeMapping
    protected void setFields(CreateBlogPostLikeDTO createBlogPostLikeDTO,
                             @MappingTarget BlogPostLike blogPostLike) {
        blogPostLike.setBlogPost(blogPostRepository
                .findById(createBlogPostLikeDTO.getBlogPostId())
                .orElseThrow(() -> new BlogPostNotFoundException("Could not find blog post with given id " +
                        createBlogPostLikeDTO.getBlogPostId())));
        blogPostLike.setUser(authenticationFacade.getCurrentUser());
    }
}
