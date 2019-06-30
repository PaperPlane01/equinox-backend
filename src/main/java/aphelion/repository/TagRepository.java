package aphelion.repository;

import aphelion.model.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag save(Tag tag);
    void delete(Tag tag);
    Optional<Tag> findByName(String name);

    @Query("select tag from Tag tag " +
            "where tag.name in :#{#names}")
    List<Tag> findAllByName(@Param("names") List<String> names);
}
