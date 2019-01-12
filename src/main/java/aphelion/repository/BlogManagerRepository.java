package aphelion.repository;

import aphelion.model.domain.Blog;
import aphelion.model.domain.BlogManager;
import aphelion.model.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BlogManagerRepository extends JpaRepository<BlogManager, Long> {
    BlogManager save(BlogManager blogManager);
    Optional<BlogManager> findById(Long id);
    void delete(BlogManager blogManager);
    List<BlogManager> findByUserAndBlog(User user, Blog blog);
    Optional<BlogManager> findByUserIdAndBlogId(Long userId, Long blogId);
    List<BlogManager> findByUser(User user, Pageable pageable);
    List<BlogManager> findAllByUser(User user);
    List<BlogManager> findByBlog(Blog blog, Pageable pageable);
    List<BlogManager> findByBlogAndUserDisplayedNameContainsIgnoreCase(Blog blog, String displayedName, Pageable pageable);
}
