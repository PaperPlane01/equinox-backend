package aphelion.repository;

import aphelion.model.domain.BlogPost;
import aphelion.model.domain.Comment;
import aphelion.model.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findById(Long id);
    Optional<Comment> findByIdAndDeleted(Long id, boolean deleted);
    Comment save(Comment comment);
    void delete(Comment comment);
    List<Comment> findByBlogPost(BlogPost blogPost, Pageable pageable);
    List<Comment> findByAuthor(User author, Pageable pageable);
    List<Comment> findByContentContaining(String line, Pageable pageable);
    List<Comment> findByReferredComment(Comment comment, Pageable pageable);
    List<Comment> findByRootComment(Comment comment, Pageable pageable);
    List<Comment> findAllByRootComment(Comment rootComment);
    List<Comment> findByBlogPostAndRoot(BlogPost blogPost, boolean root, Pageable pageable);

    @Query("select comment from Comment comment where comment.blogPost = :#{#blogPost} and comment.root = true order by comment.likes.size desc")
    List<Comment> findRootByBlogOrderByNumberOfLikesDesc(@Param("blogPost") BlogPost blogPost, Pageable pageable);

    @Query("select comment from Comment comment where comment.blogPost = :#{#blogPost} order by comment.likes.size")
    List<Comment> findByBlogPostOrderByNumberOfLikes(@Param("blogPost") BlogPost blogPost, Pageable pageable);

    @Query("select comment from Comment comment where comment.blogPost = :#{#blogPost} order by comment.likes.size desc")
    List<Comment> findByBlogPostOrderByNumberOfLikesDesc(@Param("blogPost") BlogPost blogPost, Pageable pageable);

    @Query("select comment from Comment comment where comment.rootComment = :#{#rootComment} order by comment.likes.size")
    List<Comment> findByRootCommentOrderByNumberOfLikes(@Param("rootComment") Comment rootComment, Pageable pageable);

    @Query("select comment from Comment comment where comment.rootComment = :#{#rootComment} order by comment.likes.size desc")
    List<Comment> findByRootCommentOrderByNumberOfLikesDesc(Comment rootComment, Pageable pageable);

    @Query("select comment from Comment comment where comment.blogPost = :#{#blogPost} and comment.root = true " +
            "order by comment.likes.size asc")
    List<Comment> findRootByBlogPostOrderByNumberOfLikesAsc(@Param("blogPost") BlogPost blogPost,
                                                               Pageable pageable);

    @Query("select comment from Comment comment where comment.referredComment = :#{#referredComment} order by comment.likes.size")
    List<Comment> findByReferredCommentOrderByNumberOfLikes(@Param("referredComment") Comment referredComment, Pageable pageable);
}
