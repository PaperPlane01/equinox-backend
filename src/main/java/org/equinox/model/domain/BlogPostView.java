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
public class BlogPostView {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "blogPostViewSequenceGenerator")
    @SequenceGenerator(
            name = "blogPostViewSequenceGenerator",
            sequenceName = "blogPostViewSequence",
            allocationSize = 1
    )
    private long id;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "blogPostId")
    private BlogPost blogPost;
    private Date date;
    private double ratio;
}