package io.github.minjunbaek.board.domain.board.controller.dto;

import lombok.Getter;

@Getter
public class BoardMultipleResponseDto {

  private Long id;
  private String boardName;

  public static BoardMultipleResponseDto of(Long id, String boardName) {
    BoardMultipleResponseDto responseDto = new BoardMultipleResponseDto();
    responseDto.id = id;
    responseDto.boardName = boardName;
    return responseDto;
  }
}
