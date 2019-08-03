package aphelion.repository;

import aphelion.model.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);
    User findByGeneratedUsername(String username);
    Optional<User> findByLoginUsername(String loginUserName);
    List<User> findByDisplayedNameContains(String line, Pageable pageable);
    Optional<User> findByGoogleId(String googleId);
    User save(User user);
    void delete(User user);

    @Query("select user from User user where user.personalInformation.email = :#{#email}")
    Optional<User> findByEmail(@Param("email") String email);
}
