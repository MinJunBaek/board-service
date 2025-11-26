package io.github.minjunbaek.board.domain.comment.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDto {

  @NotBlank
  private String content;
}
