package org.equinox.service;

import org.equinox.model.domain.Notification;
import org.equinox.model.domain.NotificationType;
import org.equinox.model.dto.NotificationDTO;
import org.equinox.model.dto.UpdateNotificationDTO;

import java.util.List;

public interface NotificationService {
    Notification save(Notification notification);
    void save(Long notificationGeneratorId, NotificationType notificationType);
    NotificationDTO update(Long id, UpdateNotificationDTO updateNotificationDTO);
    NotificationDTO findById(Long id);
    NotificationDTO markAsRead(Long id);
    List<NotificationDTO> markAsRead(List<Long> notificationIds);
    NotificationDTO markAsUnread(Long id);
    List<NotificationDTO> markAsUnread(List<Long> notificationIds);
    List<NotificationDTO> findByIds(List<Long> notificationIds);
    void delete(Long id);
    void delete(Notification notification);
    List<NotificationDTO> findNotificationsOfCurrentUser(int page, int pageSize, String sortingDirection, String sortBy);
    List<NotificationDTO> findNotReadNotificationsOfCurrentUser(int page, int pageSize, String sortingDirection, String sortBy);

}
