package io.github.minjunbaek.board.domain.comment.repository.entity;

import io.github.minjunbaek.board.domain.BaseEntity;
import io.github.minjunbaek.board.domain.member.repository.entity.Member;
import io.github.minjunbaek.board.domain.post.repository.entity.Post;
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
@Table(name = "comments")
@SQLDelete(sql = "UPDATE comments SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Comment extends BaseEntity {

  @Column(columnDefinition = "TEXT", nullable = false)
  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", nullable = false)
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id", nullable = false)
  private Post post;

  public static Comment create(String content, Member member, Post post) {
    Comment comment = new Comment();
    comment.content = content;
    comment.member = member;
    comment.post = post;
    return comment;
  }

  public void changeContent(String content) {
    this.content = content;
  }
}
