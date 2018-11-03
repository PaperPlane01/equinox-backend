package org.equinox.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.equinox.model.domain.Blog;
import org.equinox.model.dto.CreateBlogDTO;
import org.equinox.security.AuthenticationFacade;
import org.equinox.util.ColorUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.Date;

@Mapper
public abstract class CreateBlogDTOToBlogMapper {
    @Autowired
    private AuthenticationFacade authenticationFacade;

    @BeanMapping(resultType = Blog.class)
    public abstract Blog map(CreateBlogDTO createBlogDTO);

    @AfterMapping
    protected void setRemainingFields(@MappingTarget Blog blog) {
        blog.setCreatedAt(Date.from(Instant.now()));
        blog.setDeleted(false);
        blog.setOwner(authenticationFacade.getCurrentUser());
        blog.setLetterAvatarColor(ColorUtils.getRandomColor());
    }
}
