package io.github.minjunbaek.board.web;

import io.github.minjunbaek.board.domain.board.controller.dto.BoardResponseDto;
import io.github.minjunbaek.board.domain.board.service.BoardService;
import io.github.minjunbaek.board.domain.post.contoller.dto.PostRequestDto;
import io.github.minjunbaek.board.domain.post.contoller.dto.PostResponseDto;
import io.github.minjunbaek.board.domain.post.service.PostService;
import io.github.minjunbaek.board.security.MemberPrincipal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class PostPageController {

  private final BoardService boardService;
  private final PostService postService;

  // 게시글 등록 폼으로 이동
  @GetMapping("/boards/{boardId}/posts-form")
  public String createPostForm(
      @PathVariable(name = "boardId") Long boardId,
      @AuthenticationPrincipal MemberPrincipal memberPrincipal, Model model) {
    // 1) 게시판 목록 조회 (API에서 쓰던 로직 그대로 활용)
    List<BoardResponseDto> boards = boardService.readAllBoard();
    model.addAttribute("boards", boards);

    // 2) 로그인 상태 정보
    if (memberPrincipal != null) {
      model.addAttribute("loggedIn", true);
      model.addAttribute("memberId", memberPrincipal.getId());
      model.addAttribute("memberPrincipalName", memberPrincipal.getName()); // 필드명에 맞게 수정
    } else {
      model.addAttribute("loggedIn", false);
    }

    // 3) 내용
    model.addAttribute("selectedBoardId", boardId);

    return "post-form";
  }

  // 등록
  @PostMapping("/posts")
  public String createPost(
      @AuthenticationPrincipal MemberPrincipal memberPrincipal,
      @Validated PostRequestDto form // ★ @RequestBody 안 붙임 (폼 전송)
  ) {
    Long memberId = memberPrincipal.getId();
    postService.createPost(memberId, form);

    // 글 작성 후 해당 게시판 목록으로 리다이렉트
    return "redirect:/boards/" + form.getBoardId() + "/posts";
  }

  // 게시글 수정 폼으로 이동
  @GetMapping("/posts/{postId}/posts-form")
  public String editPostForm(
      @PathVariable(name = "postId") Long postId,
      @AuthenticationPrincipal MemberPrincipal memberPrincipal,
      Model model) {

    // 게시판 목록
    List<BoardResponseDto> boards = boardService.readAllBoard();
    model.addAttribute("boards", boards);

    // 로그인 정보
    if (memberPrincipal != null) {
      model.addAttribute("loggedIn", true);
      model.addAttribute("memberId", memberPrincipal.getId());
      model.addAttribute("memberPrincipalName", memberPrincipal.getName());
    } else {
      model.addAttribute("loggedIn", false);
    }
    PostResponseDto post = postService.editReadPost(postId);
    model.addAttribute("post", post);
    return "post-edit-form";
  }

  // 게시글 수정
  @PostMapping("/posts/{postId}")
  public String editPost(
      @PathVariable(name = "postId") Long postId,
      @AuthenticationPrincipal MemberPrincipal memberPrincipal,
      @Validated PostRequestDto postRequestDto
  ) {
    Long memberId = memberPrincipal.getId();
    postService.editPost(postId, postRequestDto, memberId);
    return "redirect:/boards/" + postRequestDto.getBoardId() + "/posts";
  }

  // 게시글 삭제
  @PostMapping("/boards/{boardId}/posts/{postId}")
  public String deletePost(
      @PathVariable(name = "boardId") Long boardId,
      @PathVariable(name = "postId") Long postId,
      @AuthenticationPrincipal MemberPrincipal memberPrincipal) {

    Long memberId = memberPrincipal.getId();
    postService.deletePost(postId, memberId);

    return "redirect:/boards/" + boardId + "/posts";
  }
}
