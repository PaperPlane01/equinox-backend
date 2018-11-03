package org.equinox.mapper;

import com.google.common.collect.Lists;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.equinox.exception.BlogNotFoundException;
import org.equinox.model.domain.BlogPost;
import org.equinox.model.domain.Tag;
import org.equinox.model.dto.CreateBlogPostDTO;
import org.equinox.repository.BlogRepository;
import org.equinox.security.AuthenticationFacade;
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

    @BeanMapping(resultType = BlogPost.class)
    @Mapping(target = "tags", ignore = true)
    public abstract BlogPost map(CreateBlogPostDTO createBlogPostDTO);

    @BeforeMapping
    protected void setBlog(CreateBlogPostDTO createBlogPostDTO,
                           @MappingTarget BlogPost blogPost) {
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
