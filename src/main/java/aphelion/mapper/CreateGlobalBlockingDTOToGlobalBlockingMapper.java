package aphelion.mapper;

import aphelion.exception.EntityNotFoundExceptionFactory;
import aphelion.model.domain.GlobalBlocking;
import aphelion.model.domain.User;
import aphelion.model.dto.CreateGlobalBlockingDTO;
import aphelion.repository.UserRepository;
import aphelion.security.AuthenticationFacade;
import org.mapstruct.BeanMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper
public abstract class CreateGlobalBlockingDTOToGlobalBlockingMapper {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Autowired
    private EntityNotFoundExceptionFactory entityNotFoundExceptionFactory;

    @BeanMapping(resultType = GlobalBlocking.class)
    public abstract GlobalBlocking map(CreateGlobalBlockingDTO createGlobalBlockingDTO);

    @BeforeMapping
    protected void setFields(CreateGlobalBlockingDTO createGlobalBlockingDTO,
                             @MappingTarget GlobalBlocking globalBlocking) {
        globalBlocking.setBlockedUser(userRepository.findById(createGlobalBlockingDTO.getBlockedUserId())
                .orElseThrow(() -> entityNotFoundExceptionFactory.create(User.class,
                        createGlobalBlockingDTO.getBlockedUserId())));
        globalBlocking.setBlockedBy(authenticationFacade.getCurrentUser());
    }
}
