package aphelion.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import aphelion.model.domain.Blog;
import aphelion.model.domain.User;
import aphelion.model.dto.CurrentUserFullProfileDTO;
import aphelion.repository.BlogBlockingRepository;
import aphelion.repository.BlogManagerRepository;
import aphelion.repository.BlogRepository;
import aphelion.repository.GlobalBlockingRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;

@Mapper(uses = AuthorityToAuthorityDTOMapper.class)
public abstract class UserToCurrentUserFullProfileDTOMapper {
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

    @BeanMapping(resultType = CurrentUserFullProfileDTO.class)
    @Mappings({
            @Mapping(source = "personalInformation.bio", target = "bio"),
            @Mapping(source = "personalInformation.birthDate", target = "birthDate"),
            @Mapping(source = "personalInformation.email", target = "email"),
            @Mapping(source = "roles", target = "authorities"),
            @Mapping(source = "avatarUri", target = "avatarUri")
    })
    public abstract CurrentUserFullProfileDTO map(User user);

    @AfterMapping
    protected void setRemainingFields(User user,
                                      @MappingTarget CurrentUserFullProfileDTO currentUserFullProfileDTO) {
        currentUserFullProfileDTO.setBlockedInBlogs(blogBlockingRepository
                .findAllByBlockedUserAndEndDateGreaterThan(user, Date.from(Instant.now()))
                .stream()
                .map(blogBlocking -> blogBlocking.getBlog().getId())
                .collect(Collectors.toList()));
        currentUserFullProfileDTO.setManagedBlogs(blogManagerRepository.findAllByUser(user)
                .stream()
                .map(blogManagerToManagedBlogDTOMapper::map)
                .collect(Collectors.toList()));
        currentUserFullProfileDTO.setBlockedGlobally(!globalBlockingRepository
                .findAllByBlockedUserAndEndDateGreaterThan(user, Date.from(Instant.now())).isEmpty());
        currentUserFullProfileDTO.setOwnedBlogs(blogRepository.findByOwner(user)
                .stream().map(Blog::getId)
                .collect(Collectors.toList()));
    }
}