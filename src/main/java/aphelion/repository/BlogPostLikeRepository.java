package aphelion.repository;

import aphelion.model.domain.BlogPost;
import aphelion.model.domain.BlogPostLike;
import aphelion.model.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BlogPostLikeRepository extends JpaRepository<BlogPostLike, Long> {
    BlogPostLike save(BlogPostLike blogPostLike);
    Optional<BlogPostLike> findById(Long id);
    void delete(BlogPostLike blogPostLike);
    Optional<BlogPostLike> findByBlogPostAndUser(BlogPost blogPost, User user);
    List<BlogPostLike> findByBlogPost(BlogPost blogPost, Pageable pageable);
    Optional<BlogPostLike> findByBlogPostIdAndUserId(Long blogPostId, Long userId);
    int countByBlogPost(BlogPost blogPost);

    @Query("select blogPostLike from BlogPostLike blogPostLike " +
            "where (blogPostLike.user = :#{#user} and not blogPostLike.blogPost.deleted = true)")
    List<BlogPostLike> findByUser(@Param("user") User user, Pageable pageable);
}
