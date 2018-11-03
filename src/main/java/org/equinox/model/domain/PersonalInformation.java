package org.equinox.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PreRemove;
import javax.persistence.SequenceGenerator;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

@Entity
@SQLDelete(sql = "update PersonalInformation set deleted = true where id = ?")
@Where(clause = "deleted = false")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PersonalInformation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "personalInformationSequenceGenerator")
    @SequenceGenerator(
            name = "personalInformationSequenceGenerator",
            sequenceName = "personalInformationSequence",
            allocationSize = 1
    )
    private long id;

    @OneToOne(mappedBy = "personalInformation", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private User user;
    private String bio;
    private Date birthDate;
    private String email;
    private boolean deleted;
    private Date deletedAt;

    public PersonalInformation(User user, String bio, Date birthDate, String email, boolean deleted, Date deletedAt) {
        this.user = user;
        this.bio = bio;
        this.birthDate = birthDate;
        this.email = email;
        this.deleted = deleted;
        this.deletedAt = deletedAt;
    }

    @PreRemove
    public void deletePersonalInformation() {
        this.deleted = true;
        this.deletedAt = Date.from(Instant.now());
    }
}