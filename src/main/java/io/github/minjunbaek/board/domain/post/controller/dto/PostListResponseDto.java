package io.github.minjunbaek.board.domain.post.controller.dto;

import lombok.Getter;

@Getter
public class PostListResponseDto {

  private Long id;
  private String title;
  private int likeCount;
  private int viewCount;
  private Long memberId;
  private String memberName;

  public static PostListResponseDto of(Long id, String title, int likeCount, int viewCount, Long memberId, String memberName) {
    PostListResponseDto dto = new PostListResponseDto();
    dto.id = id;
    dto.title = title;
    dto.likeCount = likeCount;
    dto.viewCount = viewCount;
    dto.memberId = memberId;
    dto.memberName = memberName;
    return dto;
  }
}
