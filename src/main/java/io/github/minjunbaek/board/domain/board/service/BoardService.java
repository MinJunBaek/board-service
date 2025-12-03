package io.github.minjunbaek.board.domain.board.service;

import io.github.minjunbaek.board.common.error.BoardErrorCode;
import io.github.minjunbaek.board.common.exception.ApiException;
import io.github.minjunbaek.board.domain.board.controller.dto.BoardRequestDto;
import io.github.minjunbaek.board.domain.board.controller.dto.BoardResponseDto;
import io.github.minjunbaek.board.domain.board.repository.BoardRepository;
import io.github.minjunbaek.board.domain.board.repository.entity.Board;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

  private final BoardRepository boardRepository;

  // 게시판 생성
  public void createBoard(BoardRequestDto requestDto) {
    Board board = Board.create(requestDto.getBoardName());
    boardRepository.save(board);
  }

  // 게시판 조회(단일 조회)
  @Transactional(readOnly = true)
  public BoardResponseDto readBoard(Long boardId) {
    Board board = findBoard(boardId);
    BoardResponseDto responseDto = BoardResponseDto.of(board.getId(), board.getBoardName());
    return responseDto;
  }

  // 게시판 조회(다수 조회)
  @Transactional(readOnly = true)
  public List<BoardResponseDto> readAllBoard() {
    List<BoardResponseDto> boardList = boardRepository.findAll(Sort.by(Direction.ASC, "id")).stream()
        .map(board -> BoardResponseDto.of(board.getId(), board.getBoardName())).toList();
    return boardList;
  }

  // 게시판 수정
  public BoardResponseDto editBoard(Long boardId, BoardRequestDto requestDto) {
    Board board = findBoard(boardId);
    board.changeBoardName(requestDto.getBoardName());
    return BoardResponseDto.of(board.getId(), board.getBoardName());
  }

  // 게시판 삭제
  public void deleteBoard(Long boardId) {
    Board board = findBoard(boardId);
    boardRepository.delete(board);
  }

  // 중복되는 Board를 조회하는 로직을 따로 구현
  public Board findBoard(Long boardId) {
    Board board = boardRepository.findById(boardId)
        .orElseThrow(() -> new ApiException(BoardErrorCode.BOARD_NOT_FOUND));
    return board;
  }
}
