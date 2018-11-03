package org.equinox.mapper;

import org.equinox.model.domain.Notification;
import org.equinox.model.dto.NotificationDTO;

public interface NotificationToNotificationDTOMapper {
    NotificationDTO map(Notification notification);
}
