package io.github.minjunbaek.board.domain.post.contoller.dto;

import lombok.Getter;

@Getter
public class PostResponseDto {

  private Long id;
  private String title;
  private String content;
  private int likeCount;
  private int viewCount;
  private String memberName;
  private Long boardId;

  public static PostResponseDto of(
      Long id, String title, String content, int likeCount, int viewCount, String memberName, Long boardId) {
    PostResponseDto dto = new PostResponseDto();
    dto.id = id;
    dto.title = title;
    dto.content = content;
    dto.likeCount = likeCount;
    dto.viewCount = viewCount;
    dto.memberName = memberName;
    dto.boardId = boardId;
    return dto;
  }
}
