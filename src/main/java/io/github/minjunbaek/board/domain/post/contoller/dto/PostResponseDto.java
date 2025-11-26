package io.github.minjunbaek.board.domain.post.contoller.dto;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class PostResponseDto {

  private Long id;
  private String title;
  private String content;
  private int likeCount;
  private int viewCount;
  private Long memberId;
  private String memberName;
  private Long boardId;
  private LocalDateTime updatedAt;

  public static PostResponseDto of(
      Long id, String title, String content, int likeCount, int viewCount,
      Long memberId, String memberName, Long boardId, LocalDateTime updatedAt) {
    PostResponseDto dto = new PostResponseDto();
    dto.id = id;
    dto.title = title;
    dto.content = content;
    dto.likeCount = likeCount;
    dto.viewCount = viewCount;
    dto.memberId = memberId;
    dto.memberName = memberName;
    dto.boardId = boardId;
    dto.updatedAt = updatedAt;
    return dto;
  }
}
