package org.equinox.repository;

import org.equinox.model.domain.BlogPost;
import org.equinox.model.domain.Comment;
import org.equinox.model.domain.User;
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
    List<Comment> findAllByBlogPost(BlogPost blogPost);
    List<Comment> findAllByBlogPostOrderByIdDesc(BlogPost blogPost);
    List<Comment> findByAuthor(User author, Pageable pageable);
    List<Comment> findByContentContaining(String line, Pageable pageable);
    List<Comment> findByReferredComment(Comment comment, Pageable pageable);
    List<Comment> findAllByReferredComment(Comment comment);
    List<Comment> findAllByReferredCommentOrderByIdDesc(Comment referredComment);
    List<Comment> findByRootComment(Comment comment, Pageable pageable);
    List<Comment> findAllByRootComment(Comment rootComment);
    List<Comment> findAllByRootCommentOrderByIdDesc(Comment rootComment);
    List<Comment> findByBlogPostAndRoot(BlogPost blogPost, boolean root, Pageable pageable);
    List<Comment> findAllByBlogPostAndRoot(BlogPost blogPost, boolean root);
    List<Comment> findAllByBlogPostAndRootOrderByIdDesc(BlogPost blogPost, boolean root);

    @Query("select comment from Comment comment where comment.blogPost = :#{#blogPost} and comment.root = :#{#root} order by comment.likes.size")
    List<Comment> findByBlogPostAndRootOrderByNumberOfLikes(BlogPost blogPost, boolean root, Pageable pageable);

    @Query("select comment from Comment comment where comment.blogPost = :#{#blogPost} order by comment.likes.size")
    List<Comment> findByBlogPostOrderByNumberOfLikes(@Param("blogPost") BlogPost blogPost, Pageable pageable);

    @Query("select comment from Comment comment where comment.blogPost = :#{#blogPost} order by comment.likes.size desc")
    List<Comment> findByBlogPostOrderByNumberOfLikesDesc(@Param("blogPost") BlogPost blogPost, Pageable pageable);

    @Query("select comment from Comment comment where comment.blogPost = :#{#blogPost} order by comment.likes.size asc")
    List<Comment> findAllByBlogPostOrderByNumberOfLikesAsc(@Param("blogPost") BlogPost blogPost);

    @Query("select comment from Comment comment where comment.blogPost = :#{#blogPost} order by comment.likes.size desc")
    List<Comment> findAllByBlogPostOrderByNumberOfLikesDesc(@Param("blogPost") BlogPost blogPost);

    @Query("select comment from Comment comment where comment.rootComment = :#{#rootComment} order by comment.likes.size desc")
    List<Comment> findByRootCommentOrderByNumberOfLikes(@Param("rootComment") Comment rootComment, Pageable pageable);

    @Query("select comment from Comment comment where comment.rootComment = :#{#rootComment} order by comment.likes.size asc")
    List<Comment> findAllByRootCommentOrderByNumberOfLikesAsc(@Param("rootComment") Comment rootComment);

    @Query("select comment from Comment comment where comment.rootComment = :#{#rootComment} order by comment.likes.size desc")
    List<Comment> findAllByRootCommentOrderByNumberOfLikesDesc(@Param("rootComment") Comment rootComment);

    @Query("select comment from Comment comment where comment.blogPost = :#{#blogPost} and comment.root = :#{#root} " +
            "order by comment.likes.size asc")
    List<Comment> findByBlogPostAndRootOrderByNumberOfLikesAsc(@Param("blogPost") BlogPost blogPost,
                                                                 @Param("root") boolean root, Pageable pageable);

    @Query("select comment from Comment comment " +
            "where comment.blogPost = :#{#blogPost} and comment.root = :#{#root} " +
            "order by comment.likes.size asc")
    List<Comment> findAllByBlogPostAndRootOrderByNumberOfLikesAsc(@Param("blogPost") BlogPost blogPost,
                                                                  @Param("root") boolean root);
    @Query("select comment from Comment comment " +
            "where comment.blogPost = :#{#blogPost} and comment.root = :#{#root} " +
            "order by comment.likes.size desc")
    List<Comment> findAllByBlogPostAndRootOrderByNumberOfLikesDesc(@Param("blogPost") BlogPost blogPost,
                                                                   @Param("root") boolean root);

    @Query("select comment from Comment comment where comment.referredComment = :#{#referredComment} order by comment.likes.size asc")
    List<Comment> findAllByReferredCommentOrderByNumberOfLikesAsc(@Param("referredComment") Comment referredComment);

    @Query("select comment from Comment comment where comment.referredComment = :#{#referredComment} order by comment.likes.size desc")
    List<Comment> findAllByReferredCommentOrderByNumberOfLikesDesc(@Param("referredComment") Comment referredComment);

    @Query("select comment from Comment comment where comment.referredComment = :#{#referredComment} order by comment.likes.size")
    List<Comment> findByReferredCommentOrderByNumberOfLikes(@Param("referredComment") Comment referredComment, Pageable pageable);
}
