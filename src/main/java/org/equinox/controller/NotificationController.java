package org.equinox.controller;

import lombok.RequiredArgsConstructor;
import org.equinox.model.dto.NotificationDTO;
import org.equinox.model.dto.UpdateNotificationDTO;
import org.equinox.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public List<NotificationDTO> findNotifications(@RequestParam("page") Optional<Integer> page,
                                                   @RequestParam("pageSize") Optional<Integer> pageSize,
                                                   @RequestParam("sortingDirection") Optional<String> sortingDirection,
                                                   @RequestParam("sortBy") Optional<String> sortBy) {
        return notificationService.findNotificationsOfCurrentUser(page.orElse(0),
                pageSize.orElse(30), sortingDirection.orElse("desc"),
                sortBy.orElse("id"));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/new")
    public List<NotificationDTO> findNewNotifications(@RequestParam("page") Optional<Integer> page,
                                             @RequestParam("pageSize") Optional<Integer> pageSize,
                                             @RequestParam("sortingDirection") Optional<String> sortingDirection,
                                             @RequestParam("sortBy") Optional<String> sortBy) {
        return notificationService.findNotReadNotificationsOfCurrentUser(page.orElse(0),
                pageSize.orElse(30), sortingDirection.orElse("desc"),
                sortBy.orElse("id"));
    }

    @PreAuthorize("hasRole('USER') and @notificationPermissionResolver.canDeleteNotification(#id)")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        notificationService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('USER') and @notificationPermissionResolver.canUpdateNotification(#id)")
    @PutMapping("/{id}")
    public NotificationDTO updateNotification(@PathVariable("id") Long id,
                                              @RequestBody @Valid UpdateNotificationDTO updateNotificationDTO) {
        return notificationService.update(id, updateNotificationDTO);
    }

    @PreAuthorize("hasRole('USER') " +
            "and @notificationPermissionResolver.canUpdateNotifications(#notificationIds)")
    @PatchMapping("/mark-read")
    public List<NotificationDTO> markAsRead(@RequestBody List<Long> notificationIds) {
        return notificationService.markAsRead(notificationIds);
    }

    @PreAuthorize("hasRole('USER') " +
            "and @notificationPermissionResolver.canUpdateNotifications(#notificationIds)")
    @PatchMapping("/mark-unread")
    public List<NotificationDTO> markAsUnread(@RequestBody List<Long> notificationIds) {
        return notificationService.markAsUnread(notificationIds);
    }
}
