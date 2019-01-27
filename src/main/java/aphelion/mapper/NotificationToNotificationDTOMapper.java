package aphelion.mapper;

import aphelion.model.domain.Notification;
import aphelion.model.dto.NotificationDTO;

public interface NotificationToNotificationDTOMapper {
    NotificationDTO map(Notification notification);
    NotificationDTO map(Notification notification, Object payload);
}
