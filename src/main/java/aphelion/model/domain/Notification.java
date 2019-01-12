package aphelion.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.annotation.Nullable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import java.util.Date;

@Entity
@SQLDelete(sql = "update Notification set deleted = true where id = ?")
@Where(clause = "deleted = false")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notificationSequenceGenerator")
    @SequenceGenerator(
            name = "notificationSequenceGenerator",
            sequenceName = "notificationSequence",
            allocationSize = 1
    )
    private long id;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "recipientId")
    private User recipient;
    private Long notificationGeneratorId;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;
    private Date createdAt;
    private boolean read;
    private boolean deleted;

    @Nullable
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "deletedByUserId")
    private User deletedBy;
}
