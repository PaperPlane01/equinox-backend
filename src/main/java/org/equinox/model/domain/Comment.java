package org.equinox.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@SQLDelete(sql = "update Comment set deleted = true where id = ?")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "commentSequenceGenerator")
    @SequenceGenerator(
            name = "commentSequenceGenerator",
            sequenceName = "commentSequence",
            allocationSize = 1
    )
    private long id;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "blogPostId")
    private BlogPost blogPost;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "authorId")
    private User author;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "rootCommentId")
    private Comment rootComment;
    private boolean root;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "referredCommentId")
    private Comment referredComment;

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "comment")
    @LazyCollection(LazyCollectionOption.EXTRA)
    private List<CommentLike> likes;

    @Size(max = 3500)
    private String content;
    private Date createdAt;
    private boolean deleted;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "deletedByUserId")
    private User deletedBy;
    private Date deletedAt;

    @PreRemove
    public void deleteComment() {
        this.deleted = true;
        this.deletedAt = Date.from(Instant.now());
    }

    public void removeLike(CommentLike commentLike) {
        this.likes = this.likes.stream().filter(like -> !like.equals(commentLike)).collect(Collectors.toList());;
    }
}