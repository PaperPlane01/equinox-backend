package aphelion.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BlogPostReport {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "blogPostReportSequenceGenerator")
    @SequenceGenerator(
            name = "blogPostReportSequenceGenerator",
            sequenceName = "blogPostReportSequence",
            allocationSize = 1
    )
    private long id;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "blogPostId")
    private BlogPost blogPost;
    private String description;

    @Enumerated(EnumType.STRING)
    private ReportStatus status;

    @Enumerated(EnumType.STRING)
    private ReportReason reason;

    private Date createdAt;
}
