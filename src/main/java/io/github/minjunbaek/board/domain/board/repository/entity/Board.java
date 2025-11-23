package io.github.minjunbaek.board.domain.board.repository.entity;

import io.github.minjunbaek.board.domain.BaseEntity;
import io.github.minjunbaek.board.domain.post.repository.Post;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Entity
@Table(name = "boards")
@SQLDelete(sql = "UPDATE boards SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Board extends BaseEntity {

  @Column(length = 50, nullable = false)
  private String boardName;

  // private List<Post> postList;

  public static Board createBoard(String boardName) {
    Board board = new Board();
    board.boardName = boardName;
    return board;
  }

  public void changeBoardName(String boardName) {
    this.boardName = boardName;
  }
}
