package io.github.minjunbaek.board.domain.comment.repository;

import io.github.minjunbaek.board.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "comments")
public class Comment extends BaseEntity {

  @Column(columnDefinition = "TEXT", nullable = false)
  private String content;

  // private Post post;
}
