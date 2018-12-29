package aphelion.mapper;

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
    private AuthenticationFacade authenticationFacade;

    @Autowired
    private BlogPostContentValidationService blogPostContentValidationService;

    @BeanMapping(resultType = BlogPost.class)
    @Mapping(target = "tags", ignore = true)
    public abstract BlogPost map(CreateBlogPostDTO createBlogPostDTO);

    @BeforeMapping
    protected void validateContentAndSetBlog(CreateBlogPostDTO createBlogPostDTO,
                           @MappingTarget BlogPost blogPost) {
        String text = blogPostContentValidationService.validateAndGetPlainText(createBlogPostDTO.getContent());
        blogPost.setPlainText(text);
        blogPost.setBlog(blogRepository.findById(createBlogPostDTO.getBlogId())
                .orElseThrow(() -> new BlogNotFoundException("Could not find blog with given id " +
                        createBlogPostDTO.getBlogId())));
    }

    @AfterMapping
    protected void setRemainingFields(CreateBlogPostDTO createBlogPostDTO,
                                      @MappingTarget BlogPost blogPost) {
        blogPost.setCreatedAt(Date.from(Instant.now()));
        blogPost.setDeleted(false);
        blogPost.setTags(createBlogPostDTO.getTags()
                .stream()
                .map(tagName -> {
                    Tag tag = new Tag();
                    tag.setName(tagName);
                    return tag;
                })
                .collect(Collectors.toList()));
        blogPost.setComments(Collections.emptyList());
        blogPost.setAuthor(authenticationFacade.getCurrentUser());
        blogPost.setPublishedBy(blogPost.getBlog().getDefaultPublisherType());
        blogPost.setBlogPostViews(Lists.newArrayList());
        blogPost.setLikes(Lists.newArrayList());
    }
}