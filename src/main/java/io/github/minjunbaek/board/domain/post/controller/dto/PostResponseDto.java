package io.github.minjunbaek.board.domain.post.controller.dto;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class PostResponseDto { // 이러한 DTO는 결국 도메인에 의존하기 때문에 Post를 의존하는것도 나쁘지 않은 선택일수 있음.

  private Long id;
  private String title;
  private String content;
  private int likeCount;
  private int viewCount;
  private Long memberId;
  private String memberName;
  private Long boardId;
  private LocalDateTime createdAt;

  public static PostResponseDto of(
      Long id, String title, String content, int likeCount, int viewCount,
      Long memberId, String memberName, Long boardId, LocalDateTime createAt) {
    PostResponseDto dto = new PostResponseDto();
    dto.id = id;
    dto.title = title;
    dto.content = content;
    dto.likeCount = likeCount;
    dto.viewCount = viewCount;
    dto.memberId = memberId;
    dto.memberName = memberName;
    dto.boardId = boardId;
    dto.createdAt = createAt;
    return dto;
  }
}
