package io.github.minjunbaek.board.domain.post.contoller;

import io.github.minjunbaek.board.common.api.Api;
import io.github.minjunbaek.board.domain.post.contoller.dto.PostListResponseDto;
import io.github.minjunbaek.board.domain.post.contoller.dto.PostRequestDto;
import io.github.minjunbaek.board.domain.post.contoller.dto.PostResponseDto;
import io.github.minjunbaek.board.domain.post.service.PostService;
import io.github.minjunbaek.board.security.MemberPrincipal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostController {

  private final PostService postService;

  @PostMapping("/posts")
  public ResponseEntity<Api<Void>> createPost(
      @AuthenticationPrincipal MemberPrincipal principal,
      @Validated @RequestBody PostRequestDto requestDto) {
    Long memberId = principal.getId();
    postService.createPost(memberId, requestDto);
    return ResponseEntity.ok(Api.success("CREATE_POST", "게시글 생성"));
  }

  @GetMapping("/posts/{postId}")
  public ResponseEntity<Api<PostResponseDto>> viewPost(
      @PathVariable(value = "postId") Long postId) {
    PostResponseDto postResponseDto = postService.readPost(postId);
    return ResponseEntity.ok(Api.success("VIEW_POST", "게시글 상세 조회", postResponseDto));
  }

  // 신경 써야 하는것: 권한(자신이 쓴 글만 수정하거나 삭제할수 있게 해야한다.)
  // 그렇다면 수정, 삭제요청에는 유저 ID를 받고 게시글의 저장된 Member.id와 인자값 으로 받은 유저 ID와 비교해서 일치하지 않으면 에러?
  @PatchMapping("/posts/{postId}")
  public ResponseEntity<Api<PostResponseDto>> editPost(
      @PathVariable(value = "postId") Long postId, @Validated @RequestBody PostRequestDto postRequestDto,
      @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
    Long memberId = memberPrincipal.getId();
    PostResponseDto responseDto = postService.editPost(postId, postRequestDto, memberId);
    return ResponseEntity.ok(Api.success("EDIT_POST", "게시글 수정", responseDto));
  }

  @DeleteMapping("/posts/{postId}")
  public ResponseEntity<Api<Void>> deletePost(
      @PathVariable(value = "postId") Long postId,
      @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
    Long memberId = memberPrincipal.getId();
    postService.deletePost(postId, memberId);
    return ResponseEntity.ok(Api.success("DELETE_POST", "게시글 삭제"));
  }

  @GetMapping("/{boardId}/posts")
  public ResponseEntity<Api<List<PostListResponseDto>>> viewPosts(@PathVariable(value = "boardId") Long boardId) {
    List<PostListResponseDto> postListResponseDtos = postService.readAllPost(boardId);
    return ResponseEntity.ok(Api.success("VIEW_POST_LIST", "게시글 목록 조회", postListResponseDtos));
  }

  @GetMapping("members/me/posts")
  public ResponseEntity<Api<List<PostListResponseDto>>> viewMemberPosts(
      @AuthenticationPrincipal MemberPrincipal memberPrincipal
  ) {
    List<PostListResponseDto> postListResponseDto = postService.readAllMemberPost(memberPrincipal.getId());
    return ResponseEntity.ok(Api.success("VIEW_MY_POST_LIST", "사용자 게시글 목록 조회", postListResponseDto));
  }
}
