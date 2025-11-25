package io.github.minjunbaek.board.domain.board.controller;

import io.github.minjunbaek.board.common.api.Api;
import io.github.minjunbaek.board.domain.board.controller.dto.BoardRequestDto;
import io.github.minjunbaek.board.domain.board.controller.dto.BoardResponseDto;
import io.github.minjunbaek.board.domain.board.service.BoardService;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
// @RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

  private final BoardService boardService;

  // 게시판 생성
  // @PostMapping
  public ResponseEntity<Api<Void>> create(@Validated @RequestBody BoardRequestDto requestDto) {
    boardService.createBoard(requestDto);
    return ResponseEntity.ok(Api.success("CREATE_BOARD", "게시판 생성"));
  }

  // 게시판 조회(단일 조회) -> 생각해보면 게시판의 게시글 목록이 중요하지 게시판 이름이 중요하지 않음...
  // @GetMapping("/{boardId}")
  public ResponseEntity<Api<BoardResponseDto>> read(@PathVariable(value = "boardId") Long boardId) {
    BoardResponseDto responseDto = boardService.readBoard(boardId);
    return ResponseEntity.ok(Api.success("VIEW_BOARD", "게시판 단일 조회", responseDto));
  }

  // 게시판 조회(다수 조회)
  // @GetMapping
  public ResponseEntity<Api<List<BoardResponseDto>>> readAll() {
    List<BoardResponseDto> boardList = boardService.readAllBoard();
    return ResponseEntity.ok(Api.success("VIEW_BOARD_LIST", "게시판 목록 조회", boardList));
  }

  // 게시판 수정
  // @PatchMapping("/{boardId}")
  public ResponseEntity<Api<BoardResponseDto>> editBoard(
      @PathVariable(value = "boardId") Long boardId,
      @Validated @RequestBody BoardRequestDto requestDto) {
    BoardResponseDto responseDto = boardService.editBoard(boardId, requestDto);
    return ResponseEntity.ok(Api.success("EDIT_BOARD", "게시판 수정", responseDto));
  }

  // 게시판 삭제
  // @DeleteMapping("/{boardId}")
  public ResponseEntity<Api<Void>> deleteBoard(@PathVariable(value = "boardId") Long boardId) {
    boardService.deleteBoard(boardId);
    return ResponseEntity.ok(Api.success("DELETE_BOARD", "게시판 삭제"));
  }
}
