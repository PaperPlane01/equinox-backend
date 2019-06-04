package aphelion.mapper;

import aphelion.model.domain.BlogPost;
import aphelion.model.domain.BlogPostLike;
import aphelion.model.domain.User;
import aphelion.model.dto.BlogPostDTO;
import aphelion.repository.BlogPostLikeRepository;
import aphelion.security.AuthenticationFacade;
import aphelion.security.access.BlogPostPermissionResolver;
import aphelion.util.BlogPostViewsUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
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

    @Autowired
    private BlogPostPermissionResolver blogPostPermissionResolver;

    @Autowired
    private UserToUserDTOMapper userToUserDTOMapper;

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
            blogPostDTO.setCanBeDeleted(blogPostPermissionResolver.canDeleteBlogPost(blogPostDTO, userToUserDTOMapper.map(blogPost.getAuthor())));
            blogPostDTO.setCanBeEdited(blogPostPermissionResolver.canUpdateBlogPost(blogPostDTO, userToUserDTOMapper.map(blogPost.getAuthor())));
            if (blogPostLike.isPresent()) {
                blogPostDTO.setLikedByCurrentUser(true);
                blogPostDTO.setLikeId(blogPostLike.get().getId());
            }
        } else {
            blogPostDTO.setLikedByCurrentUser(false);
            blogPostDTO.setCanBeEdited(false);
            blogPostDTO.setCanBeDeleted(false);
        }

        blogPostDTO.setNumberOfViews(BlogPostViewsUtils.countNumberOfViews(blogPost.getBlogPostViews(), 7));
        blogPostDTO.setNumberOfLikes(blogPost.getLikes().size());
        blogPostDTO.setDeletedByUserId(blogPost.isDeleted() ? blogPost.getDeletedBy().getId() : null);
    }
}
