package aphelion.repository;

import aphelion.model.domain.Blog;
import aphelion.model.domain.Subscription;
import aphelion.model.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Subscription save(Subscription subscription);
    void delete(Subscription subscription);
    Optional<Subscription> findById(Long id);
    List<Subscription> findByBlog(Blog blog, Pageable pageable);
    List<Subscription> findByUser(User user, Pageable pageable);
    Optional<Subscription> findByBlogAndUser(Blog blog, User user);
    List<Subscription> findByBlogAndUserDisplayedNameContainsIgnoreCase(Blog blog, String string, Pageable pageable);
}
