package aphelion.repository;

import aphelion.model.domain.BlogPostView;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogPostViewRepository extends JpaRepository<BlogPostView, Long> {
    BlogPostView save(BlogPostView blogPostView);
}
