package aphelion.mapper;

import aphelion.model.domain.Blog;
import aphelion.model.domain.Subscription;
import aphelion.model.domain.User;
import aphelion.model.dto.BlogDTO;
import aphelion.security.AuthenticationFacade;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import aphelion.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Mapper(uses = UserToUserMinifiedDTOMapper.class)
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
