package org.equinox.repository;

import org.equinox.model.domain.Blog;
import org.equinox.model.domain.BlogBlocking;
import org.equinox.model.domain.User;
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
}
