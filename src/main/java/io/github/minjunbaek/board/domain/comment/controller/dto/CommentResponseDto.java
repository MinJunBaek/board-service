package io.github.minjunbaek.board.domain.comment.controller.dto;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CommentResponseDto {

  private Long id;

  private String content;

  private Long memberId;

  private String memberName;

  private LocalDateTime updatedAt;

  public static CommentResponseDto of(Long id, String content, Long memberId, String memberName, LocalDateTime updatedAt) {
    CommentResponseDto commentResponseDto = new CommentResponseDto();
    commentResponseDto.id = id;
    commentResponseDto.content = content;
    commentResponseDto.memberId = memberId;
    commentResponseDto.memberName = memberName;
    commentResponseDto.updatedAt = updatedAt;
    return commentResponseDto;
  }
}
