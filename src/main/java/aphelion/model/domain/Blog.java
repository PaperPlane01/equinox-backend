package aphelion.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

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
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.SequenceGenerator;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Entity
@SQLDelete(sql = "update Blog set deleted = true where id = ?")
@Where(clause = "deleted = false")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "blogSequenceGenerator")
    @SequenceGenerator(
            name = "blogSequenceGenerator",
            sequenceName = "blogSequence",
            allocationSize = 1
    )
    private long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "ownerId")
    private User owner;
    private String name;
    private String description;

    @OneToMany(mappedBy = "blog", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private List<BlogPost> blogPosts;

    @OneToMany(mappedBy = "blog", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private List<Subscription> subscriptions;

    @Enumerated(EnumType.STRING)
    private BlogPostPublisherType defaultPublisherType;
    private Date createdAt;

    private boolean deleted;
    private Date deletedAt;
    private String avatarUri;
    private String letterAvatarColor;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "deletedById")
    private User deletedBy;

    @Enumerated(EnumType.STRING)
    private BlogManagersVisibilityLevel blogManagersVisibilityLevel;

    @PreRemove
    public void deleteBlog() {
        this.deleted = true;
        this.deletedAt = Date.from(Instant.now());
    }
}
