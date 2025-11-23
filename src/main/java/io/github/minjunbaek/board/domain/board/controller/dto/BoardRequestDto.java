package io.github.minjunbaek.board.domain.board.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class BoardRequestDto {

  @NotBlank
  private String boardName;
}
