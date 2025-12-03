package io.github.minjunbaek.board.domain.board.controller;

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
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class BoardPageController {

  private final BoardService boardService;
  private final PostService postService;

  // 특정 게시판의 게시글 목록 페이지
  @GetMapping("/boards/{boardId}/posts")
  public String boardPosts(@PathVariable Long boardId,
      @AuthenticationPrincipal MemberPrincipal memberPrincipal,
      Model model) {

    // 네비게이션용 - 게시판 목록 조회
    List<BoardResponseDto> boards = boardService.readAllBoard();
    model.addAttribute("boards", boards);

    // 네비게이션용 - 로그인 상태 정보 ==> Dto로 만들어서 model에 객체를 뿌리는게 좋을까?
    if (memberPrincipal != null) {
      model.addAttribute("loggedIn", true);
      model.addAttribute("memberId", memberPrincipal.getId());
      model.addAttribute("memberPrincipalName", memberPrincipal.getName());
    } else {
      model.addAttribute("loggedIn", false);
    }

    // 해당 게시판의 정보
    BoardResponseDto board = boardService.readBoard(boardId);
    model.addAttribute("board", board);

    // 해당 게시판의 게시글 목록
    List<PostListResponseDto> posts = postService.readAllPost(boardId);
    model.addAttribute("posts", posts);

    return "boards/view-posts";
  }
}
