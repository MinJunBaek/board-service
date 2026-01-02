package io.github.minjunbaek.board.domain.post.controller;

import io.github.minjunbaek.board.common.api.Api;
import io.github.minjunbaek.board.domain.post.controller.dto.PostListResponseDto;
import io.github.minjunbaek.board.domain.post.controller.dto.PostRequestDto;
import io.github.minjunbaek.board.domain.post.controller.dto.PostResponseDto;
import io.github.minjunbaek.board.domain.post.service.PostService;
import io.github.minjunbaek.board.security.MemberPrincipal;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated // @RequestParam, @PathVariable 검증을 위한 어노테이션
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostApiController {

  private final PostService postService;

  @PostMapping("/posts")
  public ResponseEntity<Api<Void>> createPost(
      @AuthenticationPrincipal MemberPrincipal principal,
      @Validated @RequestBody PostRequestDto requestDto
  ) {
    Long memberId = principal.getId();
    postService.createPost(memberId, requestDto);
    return ResponseEntity.ok(Api.success("CREATE_POST", "게시글 생성"));
  }

  // 전체 게시글 조회
  @GetMapping("/posts")
  public ResponseEntity<Api<Page<PostListResponseDto>>> viewPost(
      @PageableDefault(size = 10, sort = "id", direction = Direction.DESC) Pageable pageable) {
    Page<PostListResponseDto> posts = postService.readAllPost(pageable);
    return ResponseEntity.ok(Api.success("VIEW_POST_LIST", "전체 게시글 조회", posts));
  }

  @GetMapping("/posts/{postId}")
  public ResponseEntity<Api<PostResponseDto>> viewPost(
      @PathVariable(value = "postId") @Min(value = 1, message = "1 이상이어야 합니다") Long postId) {
    PostResponseDto postResponseDto = postService.readPost(postId);
    return ResponseEntity.ok(Api.success("VIEW_POST", "게시글 상세 조회", postResponseDto));
  }

  @PatchMapping("/posts/{postId}")
  public ResponseEntity<Api<PostResponseDto>> editPost(
      @PathVariable(value = "postId") @Min(value = 1, message = "1 이상이어야 합니다") Long postId, @Validated @RequestBody PostRequestDto postRequestDto,
      @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
    Long memberId = memberPrincipal.getId();
    PostResponseDto responseDto = postService.editPost(postId, postRequestDto, memberId);
    return ResponseEntity.ok(Api.success("EDIT_POST", "게시글 수정", responseDto));
  }

  @DeleteMapping("/posts/{postId}")
  public ResponseEntity<Api<Void>> deletePost(
      @PathVariable(value = "postId") @Min(value = 1, message = "1 이상이어야 합니다") Long postId,
      @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
    Long memberId = memberPrincipal.getId();
    postService.deletePost(postId, memberId);
    return ResponseEntity.ok(Api.success("DELETE_POST", "게시글 삭제"));
  }
}
