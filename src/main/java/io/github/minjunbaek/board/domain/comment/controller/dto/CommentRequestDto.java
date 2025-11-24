package io.github.minjunbaek.board.domain.comment.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommentRequestDto {

  @NotBlank
  private String content;
}
