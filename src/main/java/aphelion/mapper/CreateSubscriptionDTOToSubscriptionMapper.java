package aphelion.mapper;

import aphelion.exception.EntityNotFoundExceptionFactory;
import aphelion.model.domain.Blog;
import aphelion.model.domain.Subscription;
import aphelion.model.dto.CreateSubscriptionDTO;
import aphelion.repository.BlogRepository;
import aphelion.security.AuthenticationFacade;
import org.mapstruct.BeanMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.Date;

@Mapper
public abstract class CreateSubscriptionDTOToSubscriptionMapper {
    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Autowired
    private EntityNotFoundExceptionFactory entityNotFoundExceptionFactory;

    @BeanMapping(resultType = Subscription.class)
    public abstract Subscription map(CreateSubscriptionDTO createSubscriptionDTO);

    @BeforeMapping
    protected void setFields(CreateSubscriptionDTO createSubscriptionDTO,
                             @MappingTarget Subscription subscription) {
        subscription.setBlog(blogRepository.findById(createSubscriptionDTO.getBlogId())
                .orElseThrow(() -> entityNotFoundExceptionFactory.create(Blog.class,
                        createSubscriptionDTO.getBlogId())));
        subscription.setUser(authenticationFacade.getCurrentUser());
        subscription.setDate(Date.from(Instant.now()));
    }
}
