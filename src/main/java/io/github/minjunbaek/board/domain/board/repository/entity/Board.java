package io.github.minjunbaek.board.domain.board.repository.entity;

import io.github.minjunbaek.board.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Entity
@Table(name = "boards")
@SQLDelete(sql = "UPDATE boards SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생정자로 접근을 차단하고, JPA로 생성하기위해 PROTECTED로 설정
public class Board extends BaseEntity {

  @Column(length = 50, nullable = false)
  private String boardName;

  public static Board create(String boardName) {
    Board board = new Board();
    board.boardName = boardName;
    return board;
  }

  public void changeBoardName(String boardName) {
    this.boardName = boardName;
  }
}
