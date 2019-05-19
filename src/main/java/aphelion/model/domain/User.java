package aphelion.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.SQLDelete;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToOne;
import javax.persistence.PreRemove;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@SQLDelete(sql = "update UserDetails set deleted = true where id = ?")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "UserDetails")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userSequenceGenerator")
    @SequenceGenerator(
            name = "userSequenceGenerator",
            sequenceName = "userSequence",
            allocationSize = 1
    )
    private long id;

    private String loginUsername;
    private String generatedUsername;
    private String displayedName;

    @Enumerated(EnumType.STRING)
    private AuthType authType;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(
            name = "UsersAndAuthorities",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "authorityId")
    )
    private List<Authority> roles;

    @Cascade({
            org.hibernate.annotations.CascadeType.PERSIST,
            org.hibernate.annotations.CascadeType.DELETE,
            org.hibernate.annotations.CascadeType.SAVE_UPDATE,
            org.hibernate.annotations.CascadeType.MERGE
    })
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "personalInformationId")
    private PersonalInformation personalInformation;
    private String password;
    private boolean enabled;
    private Date disabledAt;
    private boolean locked;
    private String googleId;
    private String avatarUri;
    private String letterAvatarColor;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return generatedUsername;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @PreRemove
    public void disableUser() {
        this.enabled = false;
        this.disabledAt = Date.from(Instant.now());
        this.personalInformation.deletePersonalInformation();
    }
}