package org.equinox.repository;

import org.equinox.model.domain.Blog;
import org.equinox.model.domain.BlogManager;
import org.equinox.model.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BlogManagerRepository extends JpaRepository<BlogManager, Long> {
    BlogManager save(BlogManager blogManager);
    Optional<BlogManager> findById(Long id);
    void delete(BlogManager blogManager);
    List<BlogManager> findByUserAndBlog(User user, Blog blog);
    List<BlogManager> findByUser(User user, Pageable pageable);
    List<BlogManager> findAllByUser(User user);
    List<BlogManager> findByBlog(Blog blog, Pageable pageable);
}
