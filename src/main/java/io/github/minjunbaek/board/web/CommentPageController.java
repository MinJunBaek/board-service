package io.github.minjunbaek.board.web;

import io.github.minjunbaek.board.common.api.Api;
import io.github.minjunbaek.board.domain.comment.controller.dto.CommentRequestDto;
import io.github.minjunbaek.board.domain.comment.service.CommentService;
import io.github.minjunbaek.board.security.MemberPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class CommentPageController {

  private final CommentService commentService;

  @PostMapping("/posts/{postId}/comments")
  public String createComment(
      @PathVariable(name = "postId") Long postId,
      @AuthenticationPrincipal MemberPrincipal memberPrincipal,
      @Validated CommentRequestDto commentRequestDto) {
    Long memberId = memberPrincipal.getId();
    commentService.createComment(postId, memberId, commentRequestDto);
    return "redirect:/posts/" + postId + "/posts-form";
  }

  @PostMapping("/posts/{postId}/comments/{commentId}")
  public String deleteComment(
      @PathVariable(name = "postId") Long postId,
      @PathVariable(name = "commentId") Long commentId,
      @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
    Long memberId = memberPrincipal.getId();
    commentService.deleteComment(commentId, memberId);
    return "redirect:/posts/" + postId + "/posts-form";
  }
}
