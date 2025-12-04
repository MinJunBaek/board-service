package io.github.minjunbaek.board.domain.post.controller;

import io.github.minjunbaek.board.domain.board.controller.dto.BoardResponseDto;
import io.github.minjunbaek.board.domain.board.service.BoardService;
import io.github.minjunbaek.board.domain.comment.controller.dto.CommentResponseDto;
import io.github.minjunbaek.board.domain.comment.service.CommentService;
import io.github.minjunbaek.board.domain.post.controller.dto.PostRequestDto;
import io.github.minjunbaek.board.domain.post.controller.dto.PostResponseDto;
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
  private final CommentService commentService;

  // 게시글 등록 폼으로 이동
  @GetMapping("/boards/{boardId}/posts-form")
  public String createPostForm(
      @PathVariable(name = "boardId") Long boardId, Model model) {

    // 내용
    BoardResponseDto board = boardService.readBoard(boardId);
    model.addAttribute("selectedBoardId", board.getId());
    model.addAttribute("boardTitle", board.getBoardName());

    return "posts/post-create-form";
  }

  // 등록
  @PostMapping("/posts")
  public String createPost(
      @AuthenticationPrincipal MemberPrincipal memberPrincipal,
      @Validated PostRequestDto form // ★ @RequestBody 안 붙임 (폼 전송)
  ) {
    Long memberId = memberPrincipal.getId();
    Long postId = postService.createPost(memberId, form);

    // 글 작성 후 게시글 상세 조회 페이지로 이동
    return "redirect:/posts/" + postId + "/posts-form";
  }

  // 게시글 상세 조회 폼으로 이동
  @GetMapping("/posts/{postId}/posts-form")
  public String viewPost(
      @PathVariable(name = "postId") Long postId,
      @AuthenticationPrincipal MemberPrincipal memberPrincipal,
      Model model
  ) {

    // 게시글 조회
    PostResponseDto post = postService.readPost(postId);
    model.addAttribute("post", post);
    model.addAttribute("boardId", post.getBoardId());

    // 해당 게시글의 댓글 리스트 조회
    List<CommentResponseDto> comments = commentService.viewAllComment(postId);
    model.addAttribute("comments", comments);

    // 네비게이션용 - 로그인 상태 정보
    if (memberPrincipal != null) {
      model.addAttribute("isPostAuthor", memberPrincipal.getId().equals(post.getMemberId()));
    } else {
      model.addAttribute("isPostAuthor", false);
    }

    return "posts/post-view-form";
  }

  // 게시글 수정 폼으로 이동
  @GetMapping("/posts/{postId}/posts-edit-form")
  public String editPostForm(
      @PathVariable(name = "postId") Long postId,
      @AuthenticationPrincipal MemberPrincipal memberPrincipal,
      Model model) {

    // 게시글 수정 - 기존 게시글에 있던 데이터 불러오기
    PostResponseDto post = postService.editReadPost(postId);
    model.addAttribute("post", post);
    return "posts/post-edit-form";
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
    return "redirect:/posts/" + postId + "/posts-form";
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
