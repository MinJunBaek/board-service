package io.github.minjunbaek.board.domain.comment.repository;

import io.github.minjunbaek.board.domain.comment.repository.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long> {

  @Query("select c from Comment c join fetch c.member m where c.post.id = :postId")
  List<Comment> findAllWithMemberByPostId(Long postId);
}
