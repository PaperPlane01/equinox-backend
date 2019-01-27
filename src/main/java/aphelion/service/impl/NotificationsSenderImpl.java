package aphelion.service.impl;

import aphelion.model.dto.NotificationDTO;
import aphelion.service.NotificationsSender;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class NotificationsSenderImpl implements NotificationsSender {
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void sendNotifications(String recipientId, NotificationDTO... notifications) {
        System.out.println(recipientId);
        sendNotifications(recipientId, Arrays.stream(notifications).collect(Collectors.toList()));
    }

    @Override
    public void sendNotifications(String recipientId, List<NotificationDTO> notifications) {
        System.out.println(recipientId);
        simpMessagingTemplate.convertAndSendToUser(recipientId,
                "/notifications",
                notifications
        );
    }
}
