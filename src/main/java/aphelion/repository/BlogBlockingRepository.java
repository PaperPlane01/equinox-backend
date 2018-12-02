package aphelion.repository;

import aphelion.model.domain.Blog;
import aphelion.model.domain.BlogBlocking;
import aphelion.model.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface BlogBlockingRepository extends JpaRepository<BlogBlocking, Long> {
    BlogBlocking save(BlogBlocking blogBlocking);
    void delete(BlogBlocking blogBlocking);
    void deleteById(Long id);
    Optional<BlogBlocking> findById(Long id);
    List<BlogBlocking> findAllByBlockedUserAndBlogAndEndDateGreaterThan(User blockedUser, Blog blog, Date date);
    List<BlogBlocking> findAllByBlockedUserAndEndDateGreaterThan(User blockedUser, Date date);
    List<BlogBlocking> findByBlog(Blog blog, Pageable pageable);
    List<BlogBlocking> findByBlogAndEndDateGreaterThan(Blog blog, Date date, Pageable pageable);
    List<BlogBlocking> findByBlogAndBlockedUserDisplayedNameContains(Blog blog, String string, Pageable pageable);
    List<BlogBlocking> findByBlogAndBlockedUserDisplayedNameContainsAndEndDateGreaterThan(
            Blog blog, String string, Date date, Pageable pageable
    );
}
