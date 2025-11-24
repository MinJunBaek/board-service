package io.github.minjunbaek.board.domain.comment.controller;

import io.github.minjunbaek.board.common.api.Api;
import io.github.minjunbaek.board.domain.comment.controller.dto.CommentRequestDto;
import io.github.minjunbaek.board.domain.comment.controller.dto.CommentResponseDto;
import io.github.minjunbaek.board.domain.comment.service.CommentService;
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
public class CommentController {

  private final CommentService commentService;

  @GetMapping("/posts/{postId}/comments")
  public ResponseEntity<Api<List<CommentResponseDto>>> viewAllComment(@PathVariable(name = "postId") Long postId) {
    List<CommentResponseDto> commentResponseDtoList = commentService.viewAllComment(postId);
    return ResponseEntity.ok(Api.success("VIEW_COMMENT_LIST", "댓글 조회", commentResponseDtoList));
  }

  @PostMapping("/posts/{postId}/comments")
  public ResponseEntity<Api<Void>> createComment(
      @PathVariable(name = "postId") Long postId,
      @AuthenticationPrincipal MemberPrincipal memberPrincipal,
      @Validated @RequestBody CommentRequestDto commentRequestDto) {
    Long memberId = memberPrincipal.getId();
    commentService.createComment(postId, memberId, commentRequestDto);
    return ResponseEntity.ok(Api.success("CREATE_COMMENT", "댓글 생성"));
  }

  @PatchMapping("/comments/{commentId}")
  public ResponseEntity<Api<List<CommentResponseDto>>> editComment(
      @PathVariable(name = "postId") Long postId,
      @PathVariable(name = "commentId") Long commentId,
      @AuthenticationPrincipal MemberPrincipal memberPrincipal,
      @Validated @RequestBody CommentRequestDto commentRequestDto
  ) {
    Long memberId = memberPrincipal.getId();
    List<CommentResponseDto> commentResponseDtoList = commentService.editComment(
        postId, commentId, memberId, commentRequestDto
    );
    return ResponseEntity.ok(Api.success("EDIT_COMMENT", "댓글 수정", commentResponseDtoList));
  }

  @DeleteMapping("/comments/{commentId}")
  public ResponseEntity<Api<Void>> deleteComment(
      @PathVariable(name = "commentId") Long commentId,
      @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
    Long memberId = memberPrincipal.getId();
    commentService.deleteComment(commentId, memberId);
    return ResponseEntity.ok(Api.success("DELETE_COMMENT", "댓글 삭제"));
  }
}
