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

    // 네비게이션용 전체 게시판 목록 (boardName 사용)
    List<BoardResponseDto> boards = boardService.readAllBoard();
    model.addAttribute("boards", boards);

    // 이 게시판의 게시글 목록 (API에서 쓰던 서비스 메서드를 그대로 사용)
    List<PostListResponseDto> posts = postService.readAllPost(boardId);
    model.addAttribute("boardName", boards.get(boardId.intValue()-1).getBoardName());
    model.addAttribute("posts", posts);
    model.addAttribute("boardId", boardId);


    // 로그인 여부
    if (memberPrincipal != null) {
      model.addAttribute("loggedIn", true);
      model.addAttribute("memberId", memberPrincipal.getId());
      model.addAttribute("memberPrincipalName", memberPrincipal.getName()); // 필드명에 맞게 수정
    } else {
      model.addAttribute("loggedIn", false);
    }

    return "board-posts"; // src/main/resources/templates/board-posts.html
  }
}
