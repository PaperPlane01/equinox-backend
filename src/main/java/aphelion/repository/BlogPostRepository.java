package aphelion.repository;

import aphelion.model.domain.Blog;
import aphelion.model.domain.BlogPost;
import aphelion.model.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
    Optional<BlogPost> findById(Long id);
    Optional<BlogPost> findByIdAndDeleted(Long id, Boolean deleted);
    List<BlogPost> findByBlogAndDeletedAndDeletedBy(Blog blog, Boolean deleted, User user, Pageable pageable);
    List<BlogPost> findByBlog(Blog blog, Pageable pageable);
    List<BlogPost> findByTitleContaining(String line);
    BlogPost save(BlogPost blogPost);
    void delete(BlogPost blogPost);
    void deleteById(Long id);

    @Query("select blogPost from BlogPost blogPost where blogPost.blog = :#{#blog} order by blogPost.blogPostViews.size")
    List<BlogPost> findByBlogAndOrderByNumberOfViews(@Param("blog") Blog blog, Pageable pageable);

    @Query("select blogPost from BlogPost blogPost where blogPost.blog = :#{#blog} order by blogPost.blogPostViews.size")
    List<BlogPost> findByBlogAndOrderByNumberOfLikes(@Param("blog") Blog blog, Pageable pageable);

    @Query("select blogPost from BlogPost blogPost where blogPost.blog = :#{#blog} order by blogPost.comments.size")
    List<BlogPost> findByBlogAndOrderByNumberOfComments(@Param("blog") Blog blog, Pageable pageable);

    @Query("select blogPost from BlogPost blogPost " +
            "where blogPost.createdAt between :#{#from} and :#{#to} " +
            "order by blogPost.likes.size desc, blogPost.blogPostViews.size desc")
    List<BlogPost> findMostPopularForPeriod(@Param("from") Date from, @Param("to") Date to,
                                            Pageable pageable);
}
