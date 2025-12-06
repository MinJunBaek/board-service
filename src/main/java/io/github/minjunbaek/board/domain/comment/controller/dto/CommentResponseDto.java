package io.github.minjunbaek.board.domain.comment.controller.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class CommentResponseDto {

  private Long id;

  private String content;

  private Long memberId;

  private String memberName;

  private LocalDateTime createdAt;
}
