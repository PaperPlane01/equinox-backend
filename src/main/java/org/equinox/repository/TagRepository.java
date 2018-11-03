package org.equinox.repository;

import org.equinox.model.domain.Tag;
import org.equinox.repository.custom.TagCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long>, TagCustomRepository {
    Tag save(Tag tag);
    void delete(Tag tag);
}
