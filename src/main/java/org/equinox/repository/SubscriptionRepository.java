package org.equinox.repository;

import org.equinox.model.domain.Blog;
import org.equinox.model.domain.Subscription;
import org.equinox.model.domain.User;
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
}
