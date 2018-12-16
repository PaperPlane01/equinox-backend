package aphelion.repository;

import aphelion.model.domain.Tag;
import aphelion.repository.custom.TagCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long>, TagCustomRepository {
    Tag save(Tag tag);
    void delete(Tag tag);
}