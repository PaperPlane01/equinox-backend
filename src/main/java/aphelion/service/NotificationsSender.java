package aphelion.service;

import aphelion.model.dto.NotificationDTO;

import java.util.List;

public interface NotificationsSender {
    void sendNotifications(String recipientId, NotificationDTO... notifications);
    void sendNotifications(String recipientId, List<NotificationDTO> notifications);
}