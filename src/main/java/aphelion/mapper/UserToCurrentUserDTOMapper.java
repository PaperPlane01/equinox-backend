package aphelion.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import aphelion.model.domain.Blog;
import aphelion.model.domain.User;
import aphelion.model.dto.CurrentUserDTO;
import aphelion.repository.BlogBlockingRepository;
import aphelion.repository.BlogManagerRepository;
import aphelion.repository.BlogRepository;
import aphelion.repository.GlobalBlockingRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;

@Mapper(uses = AuthorityToAuthorityDTOMapper.class)
public abstract class UserToCurrentUserDTOMapper {
    @Autowired
    private BlogBlockingRepository blogBlockingRepository;

    @Autowired
    private GlobalBlockingRepository globalBlockingRepository;

    @Autowired
    private BlogManagerRepository blogManagerRepository;

    @Autowired
    private BlogManagerToManagedBlogDTOMapper blogManagerToManagedBlogDTOMapper;

    @Autowired
    private BlogRepository blogRepository;

    @BeanMapping(resultType = CurrentUserDTO.class)
    @Mappings({
            @Mapping(source = "roles", target = "authorities")
    })
    public abstract CurrentUserDTO map(User user);

    @BeforeMapping
    protected void setRemainingFields(User user, @MappingTarget CurrentUserDTO currentUserDTO) {
        currentUserDTO.setBlockedInBlogs(blogBlockingRepository
                .findAllByBlockedUserAndEndDateGreaterThan(user, Date.from(Instant.now()))
                .stream()
                .map(blogBlocking -> blogBlocking.getBlog().getId())
                .collect(Collectors.toList()));
        currentUserDTO.setManagedBlogs(blogManagerRepository.findAllByUser(user)
                .stream()
                .map(blogManagerToManagedBlogDTOMapper::map)
                .collect(Collectors.toList()));
        currentUserDTO.setBlockedGlobally(!globalBlockingRepository
                .findAllByBlockedUserAndEndDateGreaterThan(user, Date.from(Instant.now())).isEmpty());
        currentUserDTO.setOwnedBlogs(blogRepository.findByOwner(user)
                .stream().map(Blog::getId)
                .collect(Collectors.toList()));
    }
}
