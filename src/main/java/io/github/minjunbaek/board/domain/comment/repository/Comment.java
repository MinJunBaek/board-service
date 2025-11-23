package io.github.minjunbaek.board.domain.comment.repository;

import io.github.minjunbaek.board.domain.BaseEntity;
import io.github.minjunbaek.board.domain.post.repository.Post;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;

@Getter
@Entity(name = "comments")
public class Comment extends BaseEntity {

  @Column(columnDefinition = "TEXT", nullable = false)
  private String content;

  // private Post post;
}
