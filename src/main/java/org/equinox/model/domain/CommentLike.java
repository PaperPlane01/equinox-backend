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

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "commentLikeSequenceGenerator")
    @SequenceGenerator(
            name = "commentLikeSequenceGenerator",
            sequenceName = "commentLikeSequence",
            allocationSize = 1
    )
    private long id;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "commentId")
    private Comment comment;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    private User user;
}
