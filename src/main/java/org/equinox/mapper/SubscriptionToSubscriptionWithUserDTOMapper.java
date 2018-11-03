package org.equinox.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.equinox.model.domain.Subscription;
import org.equinox.model.dto.SubscriptionWithUserDTO;

@Mapper(uses = UserToUserMinifiedDTOMapper.class)
public interface SubscriptionToSubscriptionWithUserDTOMapper {
    @BeanMapping(resultType = SubscriptionWithUserDTO.class)
    @Mapping(source = "date", target = "subscriptionDate")
    SubscriptionWithUserDTO map(Subscription subscription);
}
