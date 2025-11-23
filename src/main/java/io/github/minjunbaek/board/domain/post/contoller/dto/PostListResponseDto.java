package io.github.minjunbaek.board.domain.post.contoller.dto;

import lombok.Getter;

@Getter
public class PostListResponseDto {

  private Long id;
  private String title;
  private int likeCount;
  private int viewCount;

  public static PostListResponseDto of(Long id, String title, int likeCount, int viewCount) {
    PostListResponseDto dto = new PostListResponseDto();
    dto.id = id;
    dto.title = title;
    dto.likeCount = likeCount;
    dto.viewCount = viewCount;
    return dto;
  }
}
