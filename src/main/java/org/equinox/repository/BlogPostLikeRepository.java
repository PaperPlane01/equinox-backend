package org.equinox.repository;

import org.equinox.model.domain.BlogPost;
import org.equinox.model.domain.BlogPostLike;
import org.equinox.model.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BlogPostLikeRepository extends JpaRepository<BlogPostLike, Long> {
    BlogPostLike save(BlogPostLike blogPostLike);
    Optional<BlogPostLike> findById(Long id);
    void delete(BlogPostLike blogPostLike);
    Optional<BlogPostLike> findByBlogPostAndUser(BlogPost blogPost, User user);
    List<BlogPostLike> findByBlogPost(BlogPost blogPost, Pageable pageable);
    List<BlogPostLike> findByUser(User user, Pageable pageable);
    Optional<BlogPostLike> findByBlogPostIdAndUserId(Long blogPostId, Long userId);
    int countByBlogPost(BlogPost blogPost);
}
