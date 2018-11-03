package org.equinox.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.equinox.model.domain.Subscription;
import org.equinox.model.dto.SubscriptionDTO;

@Mapper(uses = {BlogToBlogMinifiedDTOMapper.class, UserToUserMinifiedDTOMapper.class})
public interface SubscriptionToSubscriptionDTOMapper {
    @BeanMapping(resultType = SubscriptionDTO.class)
    SubscriptionDTO map(Subscription subscription);
}
