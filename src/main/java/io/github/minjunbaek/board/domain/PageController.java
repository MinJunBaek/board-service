package io.github.minjunbaek.board.domain;

import io.github.minjunbaek.board.domain.board.controller.dto.BoardResponseDto;
import io.github.minjunbaek.board.domain.board.service.BoardService;
import io.github.minjunbaek.board.domain.post.controller.dto.PostListResponseDto;
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

    // 네비게이션용 - 게시판 목록 조회
    List<BoardResponseDto> boards = boardService.readAllBoard();
    model.addAttribute("boards", boards);

    // 네비게이션용 - 로그인 상태 정보
    if (memberPrincipal != null) {
      model.addAttribute("loggedIn", true);
      model.addAttribute("memberId", memberPrincipal.getId());
      model.addAttribute("memberPrincipalName", memberPrincipal.getName());
    } else {
      model.addAttribute("loggedIn", false);
    }

    // 전체 게시글 목록
    List<PostListResponseDto> posts = postService.readAllPost();
    model.addAttribute("posts", posts);
    return "index";
  }

  // 메인 페이지에서 게시글 등록 폼으로 이동
  @GetMapping("/posts-form")
  public String createPostForm(
      @AuthenticationPrincipal MemberPrincipal memberPrincipal, Model model) {

    // 네비게이션용 - 게시판 목록 조회
    List<BoardResponseDto> boards = boardService.readAllBoard();
    model.addAttribute("boards", boards);

    // 네비게이션용 - 로그인 상태 정보
    if (memberPrincipal != null) {
      model.addAttribute("loggedIn", true);
      model.addAttribute("memberId", memberPrincipal.getId());
      model.addAttribute("memberPrincipalName", memberPrincipal.getName());
    } else {
      model.addAttribute("loggedIn", false);
    }

    return "posts/post-create-form";
  }
}
