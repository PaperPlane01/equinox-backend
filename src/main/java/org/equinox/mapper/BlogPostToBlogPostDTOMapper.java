package org.equinox.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.equinox.model.domain.BlogPost;
import org.equinox.model.domain.BlogPostLike;
import org.equinox.model.domain.User;
import org.equinox.model.dto.BlogPostDTO;
import org.equinox.repository.BlogPostLikeRepository;
import org.equinox.security.AuthenticationFacade;
import org.equinox.util.BlogPostViewsUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Mapper(uses = {TagToTagDTOMapper.class})
public abstract class BlogPostToBlogPostDTOMapper {
    @Autowired
    private BlogPostLikeRepository blogPostLikeRepository;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Autowired
    private BlogPostPublisherExtractor blogPostPublisherExtractor;

    @BeanMapping(resultType = BlogPostDTO.class)
    @Mapping(source = "blog.id", target = "blogId")
    public abstract BlogPostDTO map(BlogPost blogPost);

    @AfterMapping
    protected void setRemainingFields(BlogPost blogPost, @MappingTarget BlogPostDTO blogPostDTO) {
        blogPostDTO.setPublishedBy(blogPost.getPublishedBy());
        blogPostDTO.setPublisher(blogPostPublisherExtractor.getPublisher(blogPost));
        blogPostDTO.setNumberOfComments(blogPost.getComments().size());

        if (authenticationFacade.isUserAuthenticated()) {
            User currentUser = authenticationFacade.getCurrentUser();
            Optional<BlogPostLike> blogPostLike = blogPostLikeRepository.findByBlogPostAndUser(blogPost,
                    currentUser);

            if (blogPostLike.isPresent()) {
                blogPostDTO.setLikedByCurrentUser(true);
                blogPostDTO.setLikeId(blogPostLike.get().getId());
            } else {
                blogPostDTO.setLikedByCurrentUser(false);
            }
        }

        blogPostDTO.setNumberOfViews(BlogPostViewsUtils.countNumberOfViews(blogPost.getBlogPostViews(), 7));
        blogPostDTO.setNumberOfLikes(blogPost.getLikes().size());
        blogPostDTO.setDeletedByUserId(blogPost.isDeleted() ? blogPost.getDeletedBy().getId() : null);
    }
}
