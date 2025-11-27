package io.github.minjunbaek.board.web;

import io.github.minjunbaek.board.domain.board.controller.dto.BoardResponseDto;
import io.github.minjunbaek.board.domain.board.service.BoardService;
import io.github.minjunbaek.board.domain.post.contoller.dto.PostListResponseDto;
import io.github.minjunbaek.board.domain.post.service.PostService;
import io.github.minjunbaek.board.security.MemberPrincipal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PageController {

  private final BoardService boardService;
  private final PostService postService;

  // 메인 페이지
  @GetMapping("/")
  public String mainPage(@AuthenticationPrincipal MemberPrincipal memberPrincipal, Model model) {

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

    List<PostListResponseDto> posts = postService.readAllPost();
    model.addAttribute("posts", posts);
    return "index";
  }
}
