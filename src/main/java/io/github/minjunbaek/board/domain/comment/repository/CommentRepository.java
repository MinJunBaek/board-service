package io.github.minjunbaek.board.domain.comment.repository;

import io.github.minjunbaek.board.domain.comment.repository.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

  List<Comment> findAllByPostId(Long boardId);
}
