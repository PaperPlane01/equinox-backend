package aphelion.model.domain;

import aphelion.data.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.SequenceGenerator;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
@SQLDelete(sql = "update blog_post set deleted = true where id = ?")
@Where(clause = "deleted = false")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@TypeDef(name = "JsonBinaryType", typeClass = JsonBinaryType.class)
public class BlogPost {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "blogPostSequenceGenerator")
    @SequenceGenerator(
            name = "blogPostSequenceGenerator",
            sequenceName = "blogPostSequence",
            allocationSize = 1
    )
    private long id;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "authorId")
    private User author;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "blogId")
    private Blog blog;
    private String title;

    @Column(columnDefinition = "jsonb")
    @Type(type = "JsonBinaryType")
    private Map<Object, Object> content;
    private Date createdAt;

    @OneToMany(mappedBy = "blogPost", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private List<BlogPostLike> likes;

    @OneToMany(mappedBy = "blogPost", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private List<Comment> comments;
    private boolean deleted;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "deletedByUserId")
    private User deletedBy;
    private Date deletedAt;

    @Enumerated(EnumType.STRING)
    private BlogPostPublisherType publishedBy;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(name = "BlogPostsAndTags",
            joinColumns = @JoinColumn(name = "blogPostId"),
            inverseJoinColumns = @JoinColumn(name = "tagId"))
    private Collection<Tag> tags;

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "blogPost")
    @LazyCollection(LazyCollectionOption.EXTRA)
    private Collection<BlogPostView> blogPostViews;

    @Column(columnDefinition = "TEXT")
    private String plainText;
    private boolean pinned;
    private Date pinDate;

    @PreRemove
    public void deleteBlogPost() {
        this.deleted = true;
        this.deletedAt = Date.from(Instant.now());
    }

    public void removeBlogPostLike(BlogPostLike blogPostLike) {
        likes = likes.stream().filter(like -> !like.equals(blogPostLike)).collect(Collectors.toList());
    }
}