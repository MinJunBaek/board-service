package io.github.minjunbaek.board.domain;

import io.github.minjunbaek.board.domain.post.controller.dto.PostListResponseDto;
import io.github.minjunbaek.board.domain.post.service.PostService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PageController {
  private final PostService postService;

  // 메인 페이지
  @GetMapping("/")
  public String mainPage(Model model) {
    // 전체 게시글 목록
    List<PostListResponseDto> posts = postService.readAllPost();
    model.addAttribute("posts", posts);
    return "index";
  }

  // 메인 페이지에서 게시글 등록 폼으로 이동
  @GetMapping("/posts-form") // 메인페이지는 boardId가 없기때문에 리다이랙트를 시켜주는 용도
  public String createPostForm() {
    return "posts/post-create-form";
  }
}
