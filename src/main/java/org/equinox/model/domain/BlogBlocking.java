package org.equinox.model.domain;

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
import javax.persistence.SequenceGenerator;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BlogBlocking {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "blogBlockingSequenceGenerator")
    @SequenceGenerator(
            name = "blogBlockingSequenceGenerator",
            sequenceName = "blogBlockingSequence",
            allocationSize = 1
    )
    private long id;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "blogId")
    private Blog blog;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "blockedUserId")
    private User blockedUser;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "blockedById")
    private User blockedBy;
    private String reason;
    private Date startDate;
    private Date endDate;
}
