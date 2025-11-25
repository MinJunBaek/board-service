package io.github.minjunbaek.board.web;

import io.github.minjunbaek.board.domain.board.controller.dto.BoardResponseDto;
import io.github.minjunbaek.board.domain.board.service.BoardService;
import io.github.minjunbaek.board.domain.post.contoller.dto.PostRequestDto;
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

  // 게시글 등록 이동
  @GetMapping("/{boardId}/posts")
  public String createPost(@PathVariable(name = "boardId") Long boardId,
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
  public String createPostFromForm(
      @AuthenticationPrincipal MemberPrincipal principal,
      @Validated PostRequestDto form // ★ @RequestBody 안 붙임 (폼 전송)
  ) {
    Long memberId = principal.getId();
    postService.createPost(memberId, form);

    // 글 작성 후 해당 게시판 목록으로 리다이렉트
    return "redirect:/boards/" + form.getBoardId() + "/posts";
  }

  // 수정

  // 삭제
}
