package org.equinox.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.equinox.model.domain.Blog;
import org.equinox.model.domain.Subscription;
import org.equinox.model.domain.User;
import org.equinox.model.dto.BlogDTO;
import org.equinox.repository.SubscriptionRepository;
import org.equinox.security.AuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Mapper(uses = UserToUserDTOMapper.class)
public abstract class BlogToBlogDTOMapper {
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @BeanMapping(resultType = BlogDTO.class)
    public abstract BlogDTO map(Blog blog);

    @AfterMapping
    protected void setRemainingFields(Blog blog, @MappingTarget BlogDTO blogDTO) {
        boolean currentUserSubscribedToBlog = false;
        Long subscriptionId = null;

        if (authenticationFacade.isUserAuthenticated()) {
            User currentUser = authenticationFacade.getCurrentUser();
            Optional<Subscription> subscription = subscriptionRepository.findByBlogAndUser(blog,
                    currentUser);
            if (subscription.isPresent()) {
                currentUserSubscribedToBlog = true;
                subscriptionId = subscription.get().getId();
            }
        }

        blogDTO.setCurrentUserSubscribed(currentUserSubscribedToBlog);
        blogDTO.setSubscriptionId(subscriptionId);
    }
}
