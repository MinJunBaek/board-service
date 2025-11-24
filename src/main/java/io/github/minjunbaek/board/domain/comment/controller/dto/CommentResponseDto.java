package io.github.minjunbaek.board.domain.comment.controller.dto;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CommentResponseDto {

  private Long commentId;

  private String content;

  private String email; // 유저의 정보는 id와 닉네임(실제로 보여지는 데이터)로 고려해보자

  private LocalDateTime updatedAt;

  public static CommentResponseDto of(Long commentId, String content, String email, LocalDateTime updatedAt) {
    CommentResponseDto commentResponseDto = new CommentResponseDto();
    commentResponseDto.commentId = commentId;
    commentResponseDto.content = content;
    commentResponseDto.email = email;
    commentResponseDto.updatedAt = updatedAt;
    return commentResponseDto;
  }
}
