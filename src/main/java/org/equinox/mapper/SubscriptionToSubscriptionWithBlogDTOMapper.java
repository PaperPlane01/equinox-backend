package org.equinox.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.equinox.model.domain.Subscription;
import org.equinox.model.dto.SubscriptionWithBlogDTO;

@Mapper(uses = BlogToBlogMinifiedDTOMapper.class)
public interface SubscriptionToSubscriptionWithBlogDTOMapper {
    @BeanMapping(resultType = SubscriptionWithBlogDTO.class)
    @Mapping(source = "date", target = "subscriptionDate")
    SubscriptionWithBlogDTO map(Subscription subscription);
}