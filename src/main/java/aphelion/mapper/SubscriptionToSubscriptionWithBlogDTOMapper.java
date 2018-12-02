package aphelion.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import aphelion.model.domain.Subscription;
import aphelion.model.dto.SubscriptionWithBlogDTO;

@Mapper(uses = BlogToBlogMinifiedDTOMapper.class)
public interface SubscriptionToSubscriptionWithBlogDTOMapper {
    @BeanMapping(resultType = SubscriptionWithBlogDTO.class)
    @Mapping(source = "date", target = "subscriptionDate")
    SubscriptionWithBlogDTO map(Subscription subscription);
}