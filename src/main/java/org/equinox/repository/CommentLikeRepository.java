package org.equinox.repository;

import org.equinox.model.domain.Comment;
import org.equinox.model.domain.CommentLike;
import org.equinox.model.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    CommentLike save(CommentLike commentLike);
    Optional<CommentLike> findById(Long id);
    void delete(CommentLike commentLike);
    void deleteById(Long id);
    Optional<CommentLike> findByUserAndComment(User user, Comment comment);
    List<CommentLike> findByComment(Comment comment, Pageable pageable);
    List<CommentLike> findByUser(User user, Pageable pageable);
    Integer countAllByComment(Comment comment);
}
