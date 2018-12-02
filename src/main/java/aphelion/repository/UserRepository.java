package aphelion.repository;

import aphelion.model.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);
    User findByGeneratedUsername(String username);
    Optional<User> findByLoginUsername(String loginUserName);
    List<User> findByDisplayedNameContains(String line, Pageable pageable);
    Optional<User> findByVkId(String vkId);
    Optional<User> findByGoogleId(String googleId);
    User save(User user);
    void delete(User user);
}
