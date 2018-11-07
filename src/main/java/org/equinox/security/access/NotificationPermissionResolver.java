package org.equinox.security.access;

import lombok.RequiredArgsConstructor;
import org.equinox.model.dto.CurrentUserDTO;
import org.equinox.model.dto.NotificationDTO;
import org.equinox.service.NotificationService;
import org.equinox.service.UserService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class NotificationPermissionResolver {
    private final NotificationService notificationService;
    private final UserService userService;

    public boolean canUpdateNotification(Long notificationId) {
        NotificationDTO notificationDTO = notificationService.findById(notificationId);
        CurrentUserDTO currentUser = userService.getCurrentUser();

        return Objects.equals(notificationDTO.getRecipientId(), currentUser.getId());
    }

    public boolean canUpdateNotifications(List<Long> notificationIds) {
        List<NotificationDTO> notifications = notificationService.findByIds(notificationIds);
        CurrentUserDTO currentUser = userService.getCurrentUser();

        return notifications.stream().allMatch(notification ->
                Objects.equals(notification.getRecipientId(), currentUser.getId()));
    }

    public boolean canDeleteNotification(Long notificationId) {
        NotificationDTO notification = notificationService.findById(notificationId);
        CurrentUserDTO currentUser = userService.getCurrentUser();

        return Objects.equals(notification.getRecipientId(), currentUser.getId());
    }
}
