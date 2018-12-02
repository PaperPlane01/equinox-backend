package aphelion.mapper;

import aphelion.model.domain.Subscription;
import aphelion.model.dto.SubscriptionWithUserDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = UserToUserMinifiedDTOMapper.class)
public interface SubscriptionToSubscriptionWithUserDTOMapper {
    @BeanMapping(resultType = SubscriptionWithUserDTO.class)
    @Mapping(source = "date", target = "subscriptionDate")
    @Mapping(source = "blog.id", target = "blogId")
    SubscriptionWithUserDTO map(Subscription subscription);
}
