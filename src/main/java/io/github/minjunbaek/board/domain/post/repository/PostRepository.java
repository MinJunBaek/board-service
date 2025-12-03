package io.github.minjunbaek.board.domain.post.repository;

import io.github.minjunbaek.board.domain.post.repository.entity.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

  // 쿼리문은 JPQL로 DB의 테이블명이 아닌 Java의 엔티티명으로 쿼리문을 작성해야 한다.
  @Query("select p from Post p join fetch p.member m where m.id = :memberId order by p.id desc")
  List<Post> findAllWithMemberByMemberId(@Param("memberId") Long memberId);

  @Query("select p from Post p join fetch p.member m where p.board.id = :boardId order by p.id desc")
  List<Post> findAllWithMemberByBoardId(@Param("boardId") Long boardId);

  @Query("select p from Post p join fetch p.member order by p.id desc")
  List<Post> findAllWithMember();
}
