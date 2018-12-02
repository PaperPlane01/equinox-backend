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

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BlogManager {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "blogManagerSequenceGenerator")
    @SequenceGenerator(
            name = "blogManagerSequenceGenerator",
            sequenceName = "blogManagerSequence",
            allocationSize = 1
    )
    private long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "blogId")
    private Blog blog;

    @Enumerated(EnumType.STRING)
    private BlogRole blogRole;
}
