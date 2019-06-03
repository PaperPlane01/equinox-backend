package aphelion.mapper;

import aphelion.model.domain.Blog;
import aphelion.repository.TagRepository;
import aphelion.service.BlogPostContentValidationService;
import com.google.common.collect.Lists;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import aphelion.exception.BlogNotFoundException;
import aphelion.model.domain.BlogPost;
import aphelion.model.domain.Tag;
import aphelion.model.dto.CreateBlogPostDTO;
import aphelion.repository.BlogRepository;
import aphelion.security.AuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

@Mapper
public abstract class CreateBlogPostDTOToBlogPostMapper {
    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Autowired
    private BlogPostContentValidationService blogPostContentValidationService;

    @BeanMapping(resultType = BlogPost.class)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "publishedBy", ignore = true)
    public abstract BlogPost map(CreateBlogPostDTO createBlogPostDTO);

    @BeforeMapping
    protected void validateContentAndSetBlog(CreateBlogPostDTO createBlogPostDTO, @MappingTarget BlogPost blogPost) {
        String text = blogPostContentValidationService.validateAndGetPlainText(createBlogPostDTO.getContent());
        Blog blog = blogRepository.findById(createBlogPostDTO.getBlogId())
                .orElseThrow(() -> new BlogNotFoundException("Could not find blog with given id " +
                        createBlogPostDTO.getBlogId()));
        blogPost.setPlainText(text);
        blogPost.setBlog(blog);
        if (createBlogPostDTO.getPublishedBy() == null) {
            blogPost.setPublishedBy(blog.getDefaultPublisherType());
        } else {
            blogPost.setPublishedBy(createBlogPostDTO.getPublishedBy());
        }
    }

    @AfterMapping
    protected void setRemainingFields(@MappingTarget BlogPost blogPost) {
        blogPost.setCreatedAt(Date.from(Instant.now()));
        blogPost.setDeleted(false);
        blogPost.setComments(Collections.emptyList());
        blogPost.setAuthor(authenticationFacade.getCurrentUser());
        blogPost.setBlogPostViews(Lists.newArrayList());
        blogPost.setLikes(Lists.newArrayList());
    }
}