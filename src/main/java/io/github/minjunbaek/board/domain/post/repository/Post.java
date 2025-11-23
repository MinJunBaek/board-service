package io.github.minjunbaek.board.domain.post.repository;

import io.github.minjunbaek.board.domain.BaseEntity;
import io.github.minjunbaek.board.domain.board.repository.entity.Board;
import io.github.minjunbaek.board.domain.comment.repository.Comment;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.util.List;
import lombok.Getter;

@Getter
@Entity(name = "posts")
public class Post extends BaseEntity {

  @Column(length = 100, nullable = false)
  private String title;

  @Column(columnDefinition = "TEXT", nullable = false)
  private String content;

  private int likeContent;

  private int viewCount;

  // private Board board;

  // private List<Comment> commentList;
}
