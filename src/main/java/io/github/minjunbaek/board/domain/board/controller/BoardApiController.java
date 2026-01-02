package io.github.minjunbaek.board.domain.board.controller;

import io.github.minjunbaek.board.common.api.Api;
import io.github.minjunbaek.board.domain.board.controller.dto.BoardRequestDto;
import io.github.minjunbaek.board.domain.board.controller.dto.BoardResponseDto;
import io.github.minjunbaek.board.domain.board.service.BoardService;
import io.github.minjunbaek.board.domain.post.controller.dto.PostListResponseDto;
import io.github.minjunbaek.board.domain.post.service.PostService;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardApiController {

  private final BoardService boardService;
  private final PostService postService;

  // 게시판 생성
  @PostMapping
  public ResponseEntity<Api<Void>> create(@Validated @RequestBody BoardRequestDto requestDto) {
    Long createdId = boardService.createBoard(requestDto);
    URI location = URI.create("/api/boards/" + createdId);
    return ResponseEntity.created(location).body(Api.success("CREATE_BOARD", "게시판 생성"));
  }

  // 게시판 조회(단일 조회) -> 생각해보면 게시판의 게시글 목록이 중요하지 게시판 이름이 중요하지 않음...
  @GetMapping("/{boardId}")
  public ResponseEntity<Api<BoardResponseDto>> read(@PathVariable(value = "boardId") Long boardId) {
    BoardResponseDto responseDto = boardService.readBoard(boardId);
    return ResponseEntity.ok(Api.success("VIEW_BOARD", "게시판 단일 조회", responseDto));
  }

  // 게시판 조회(다수 조회)
  @GetMapping
  public ResponseEntity<Api<List<BoardResponseDto>>> readAll() {
    List<BoardResponseDto> boardList = boardService.readAllBoard();
    return ResponseEntity.ok(Api.success("VIEW_BOARD_LIST", "게시판 목록 조회", boardList));
  }

  // 게시판 수정
  @PatchMapping("/{boardId}")
  public ResponseEntity<Api<BoardResponseDto>> editBoard(
      @PathVariable(value = "boardId") Long boardId,
      @Validated @RequestBody BoardRequestDto requestDto) {
    BoardResponseDto responseDto = boardService.editBoard(boardId, requestDto);
    return ResponseEntity.ok(Api.success("EDIT_BOARD", "게시판 수정", responseDto));
  }

  // 게시판 삭제
  @DeleteMapping("/{boardId}")
  public ResponseEntity<Api<Void>> deleteBoard(@PathVariable(value = "boardId") Long boardId) {
    boardService.deleteBoard(boardId);
    return ResponseEntity.ok(Api.success("DELETE_BOARD", "게시판 삭제"));
  }

  // 게시판의 게시글 목록 조회
  @GetMapping("/{boardId}/posts")
  public ResponseEntity<Api<Page<PostListResponseDto>>> viewPosts(
      @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
      @PathVariable(value = "boardId") Long boardId) {
    Page<PostListResponseDto> postsPageResponseDtos = postService.readAllPost(pageable, boardId);
    return ResponseEntity.ok(Api.success("VIEW_POST_LIST", "게시글 목록 조회", postsPageResponseDtos));
  }
}
