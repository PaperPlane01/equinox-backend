package aphelion.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import aphelion.model.domain.Subscription;
import aphelion.model.dto.SubscriptionDTO;

@Mapper(uses = {BlogToBlogMinifiedDTOMapper.class, UserToUserMinifiedDTOMapper.class})
public interface SubscriptionToSubscriptionDTOMapper {
    @BeanMapping(resultType = SubscriptionDTO.class)
    SubscriptionDTO map(Subscription subscription);
}
