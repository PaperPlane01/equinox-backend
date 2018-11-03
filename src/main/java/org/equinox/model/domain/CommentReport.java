package org.equinox.model.domain;

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

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentReport {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "commentReportSequenceGenerator")
    @SequenceGenerator(
            name = "commentReportSequenceGenerator",
            sequenceName = "commentReportSequence",
            allocationSize = 1
    )
    private long id;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "commentId")
    private Comment comment;
    private String content;

    @Enumerated(EnumType.STRING)
    private ReportStatus status;

    @Enumerated(EnumType.STRING)
    private ReportReason reason;

    public CommentReport(Comment comment, String content, ReportStatus status, ReportReason reason) {
        this.comment = comment;
        this.content = content;
        this.status = status;
        this.reason = reason;
    }
}
