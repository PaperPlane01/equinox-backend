package aphelion.mapper;

import aphelion.exception.UserNotFoundException;
import org.mapstruct.BeanMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import aphelion.exception.BlogNotFoundException;
import aphelion.model.domain.BlogManager;
import aphelion.model.dto.CreateBlogManagerDTO;
import aphelion.repository.BlogRepository;
import aphelion.repository.UserRepository;
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
