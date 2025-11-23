package io.github.minjunbaek.board.domain.board.controller.dto;

import lombok.Getter;

@Getter
public class BoardResponseDto {

  private Long id;
  private String boardName;

  public static BoardResponseDto of(Long id, String boardName) {
    BoardResponseDto responseDto = new BoardResponseDto();
    responseDto.id = id;
    responseDto.boardName = boardName;
    return responseDto;
  }
}
