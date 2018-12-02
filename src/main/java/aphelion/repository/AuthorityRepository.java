package aphelion.repository;

import aphelion.model.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Optional<Authority> findById(Long id);
    Authority save(Authority authority);
    void delete(Authority authority);
    Authority findByName(String name);
}
