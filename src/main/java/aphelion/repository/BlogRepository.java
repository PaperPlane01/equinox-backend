package aphelion.repository;

import aphelion.model.domain.Blog;
import aphelion.model.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    Optional<Blog> findById(Long id);
    Blog save(Blog blog);
    void delete(Blog blog);
    List<Blog> findByOwner(User owner);

    @Query("select blog from Blog blog where blog.deleted = true and blog.id = :#{#id}")
    Optional<Blog> findDeletedById(@Param("id") Long id);
}
