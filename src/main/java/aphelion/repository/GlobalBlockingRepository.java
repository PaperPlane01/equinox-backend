package aphelion.repository;

import aphelion.model.domain.GlobalBlocking;
import aphelion.model.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface GlobalBlockingRepository extends JpaRepository<GlobalBlocking, Long> {
    GlobalBlocking save(GlobalBlocking globalBlocking);
    void delete(GlobalBlocking globalBlocking);
    Optional<GlobalBlocking> findById(Long id);
    List<GlobalBlocking> findByBlockedUser(User user, Pageable pageable);
    List<GlobalBlocking> findAllByBlockedUserAndEndDateGreaterThan(User user, Date date);
    List<GlobalBlocking> findByBlockedBy(User user, Pageable pageable);
    List<GlobalBlocking> findAllByBlockedByAndEndDateGreaterThan(User user, Date date);
}
