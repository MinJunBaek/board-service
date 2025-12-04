package io.github.minjunbaek.board.domain.board.controller;

import io.github.minjunbaek.board.domain.board.controller.dto.BoardResponseDto;
import io.github.minjunbaek.board.domain.board.service.BoardService;
import io.github.minjunbaek.board.domain.post.controller.dto.PostListResponseDto;
import io.github.minjunbaek.board.domain.post.service.PostService;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
  public String boardPosts(@PathVariable Long boardId, Model model) {
    // 해당 게시판의 정보
    BoardResponseDto board = boardService.readBoard(boardId);
    model.addAttribute("board", board);

    // 해당 게시판의 게시글 목록
    List<PostListResponseDto> posts = postService.readAllPost(boardId);
    model.addAttribute("posts", posts);

    return "boards/view-posts";
  }
}
