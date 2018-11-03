package org.equinox.mapper;

import org.equinox.exception.UserNotFoundException;
import org.mapstruct.BeanMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.equinox.exception.BlogNotFoundException;
import org.equinox.model.domain.BlogManager;
import org.equinox.model.dto.CreateBlogManagerDTO;
import org.equinox.repository.BlogRepository;
import org.equinox.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper
public abstract class CreateBlogManagerDTOToBlogManagerMapper {
    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private UserRepository userRepository;

    @BeanMapping(resultType = BlogManager.class)
    public abstract BlogManager map(CreateBlogManagerDTO createBlogManagerDTO);

    @BeforeMapping
    protected void setFields(CreateBlogManagerDTO createBlogManagerDTO,
                             @MappingTarget BlogManager blogManager) {
        blogManager.setUser(userRepository.findById(createBlogManagerDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("Could not find user with id "
                        + createBlogManagerDTO.getUserId())));
        blogManager.setBlog(blogRepository.findById(createBlogManagerDTO.getBlogId())
                .orElseThrow(() -> new BlogNotFoundException("Could not find blog with id "
                        + createBlogManagerDTO.getBlogId())));
    }
}
