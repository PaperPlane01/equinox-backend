package aphelion.repository;

import aphelion.model.domain.Notification;
import aphelion.model.domain.NotificationType;
import aphelion.model.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Notification save(Notification notification);
    void delete(Notification notification);
    void deleteAllByNotificationGeneratorIdAndNotificationType(Long id, NotificationType notificationType);
    Optional<Notification> findById(Notification notification);
    List<Notification> findByNotificationType(NotificationType notificationType, Pageable pageable);
    List<Notification> findByRecipient(User recipient, Pageable pageable);
    List<Notification> findByRecipientAndRead(User recipient, boolean read, Pageable pageable);
    List<Notification> findByRecipientAndNotificationType(User recipient, NotificationType notificationType, Pageable pageable);

    @Query("select notification from Notification notification where notification.id in :#{#ids}")
    List<Notification> findByIdWithinList(@Param("ids") List<Long> ids);
}
