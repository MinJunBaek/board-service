package io.github.minjunbaek.board.domain.post.repository;

import io.github.minjunbaek.board.domain.post.repository.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

  /**쿼리문은 JPQL로 DB의 테이블명이 아닌 Java의 엔티티명으로 쿼리문을 작성해야 한다.*/
  // 회원별 게시글 목록 조회
  @Query(value = "select p from Post p join fetch p.member m where m.id = :memberId",
      countQuery = "select count(p) from Post p where p.member.id = :memberId")
  Page<Post> findAllWithMemberByMemberId(Pageable pageable, @Param("memberId") Long memberId);

  // 게시판별 게시글 목록 조회
  @Query(value = "select p from Post p join fetch p.member m where p.board.id = :boardId",
  countQuery = "select count(p) from Post p where p.board.id = :boardId")
  Page<Post> findAllWithMemberByBoardId(Pageable pageable, @Param("boardId") Long boardId);

  // 전체 게시글 목록 조회
  @Query(value = "select p from Post p join fetch p.member m",
  countQuery = "select count(p) from Post p")
  Page<Post> findAllWithMember(Pageable pageable);
}
