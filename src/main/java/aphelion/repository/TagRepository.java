package aphelion.repository;

import aphelion.model.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag save(Tag tag);
    void delete(Tag tag);
    Optional<Tag> findByName(String name);
}
