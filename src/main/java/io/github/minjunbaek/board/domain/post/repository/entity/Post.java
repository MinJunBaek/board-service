package io.github.minjunbaek.board.domain.post.repository.entity;

import io.github.minjunbaek.board.domain.BaseEntity;
import io.github.minjunbaek.board.domain.board.repository.entity.Board;
import io.github.minjunbaek.board.domain.member.repository.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Entity
@Table(name = "posts")
@SQLDelete(sql = "UPDATE posts SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Post extends BaseEntity {

  @Column(length = 100, nullable = false)
  private String title;

  @Column(columnDefinition = "TEXT", nullable = false)
  private String content;

  private int likeCount;

  private int viewCount;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", nullable = false)
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "board_id", nullable = false)
  private Board board;

  public static Post create(String title, String content, Member member, Board board) {
    Post post = new Post();
    post.title = title;
    post.content = content;
    post.member = member;
    post.board = board;
    return post;
  }

  public void changeTitle(String title) {
    this.title = title;
  }

  public void changeContent(String content) {
    this.content = content;
  }

  public void changeBoard(Board board) {
    this.board = board;
  }

  public void increaseViewCount() {
    this.viewCount++;
  }

  public void increaseLikeCount() {
    this.likeCount++;
  }
}
