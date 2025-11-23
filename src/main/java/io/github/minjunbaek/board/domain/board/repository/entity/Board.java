package io.github.minjunbaek.board.domain.board.repository.entity;

import io.github.minjunbaek.board.domain.BaseEntity;
import io.github.minjunbaek.board.domain.post.repository.Post;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.util.List;
import lombok.Getter;

@Getter
@Entity(name = "boards")
public class Board extends BaseEntity {

  @Column(length = 50, nullable = false)
  private String boardName;

  private List<Post> postList;
}
