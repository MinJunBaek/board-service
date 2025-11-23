package io.github.minjunbaek.board.domain.post.repository;

import io.github.minjunbaek.board.domain.post.repository.entity.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

  List<Post> findAllByMemberId(Long memberId);

  List<Post> findAllByBoardId(Long boardId);
}
