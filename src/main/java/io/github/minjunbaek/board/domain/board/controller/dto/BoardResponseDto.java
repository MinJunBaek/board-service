package io.github.minjunbaek.board.domain.board.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of") // staticName이 붙으면 자동으로 생성자의 접근제어자는 private 이 된다.
public class BoardResponseDto {

  private Long id;
  private String boardName;

  /* @AllArgsConstructor 어노테이션은 아래와 같은 코드.
  private BoardResponseDto(Long id, String boardName) {
    this.id = id;
    this.boardName = boardName;
  }

  public static BoardResponseDto of(Long id, String boardName) {
    return new BoardResponseDto(id, boardName);
  }
  */
}