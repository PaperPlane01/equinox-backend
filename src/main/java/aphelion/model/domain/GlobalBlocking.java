package aphelion.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import java.time.Instant;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GlobalBlocking {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "globalBlockingSequenceGenerator")
    @SequenceGenerator(
            name = "globalBlockingSequenceGenerator",
            sequenceName = "globalBlockingSequence",
            allocationSize = 1
    )
    private long id;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "blockedUserId")
    private User blockedUser;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "blockedById")
    private User blockedBy;
    private String reason;
    private Date startDate;
    private Date endDate;

    public GlobalBlocking(User blockedUser, User blockedBy, String reason, Date startDate, Date endDate) {
        this.blockedUser = blockedUser;
        this.blockedBy = blockedBy;
        this.reason = reason;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @PrePersist
    public void prePersist() {
        this.startDate = Date.from(Instant.now());
    }
}
