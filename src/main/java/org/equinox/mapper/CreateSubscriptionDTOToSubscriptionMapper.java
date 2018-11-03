package org.equinox.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.equinox.exception.EntityNotFoundExceptionFactory;
import org.equinox.model.domain.Blog;
import org.equinox.model.domain.Subscription;
import org.equinox.model.dto.CreateSubscriptionDTO;
import org.equinox.repository.BlogRepository;
import org.equinox.security.AuthenticationFacade;
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
