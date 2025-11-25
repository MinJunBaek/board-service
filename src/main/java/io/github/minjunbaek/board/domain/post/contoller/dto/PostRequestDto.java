package io.github.minjunbaek.board.domain.post.contoller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostRequestDto {

  @NotNull
  private Long boardId;

  @NotBlank
  private String title;

  @NotBlank
  private String content;
}
