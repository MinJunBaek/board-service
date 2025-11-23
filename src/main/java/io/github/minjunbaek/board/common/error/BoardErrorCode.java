package io.github.minjunbaek.board.common.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BoardErrorCode implements ErrorCodeInterface{
  BOARD_NOT_FOUND(HttpStatus.NOT_FOUND,"BOARD_NOT_FOUND", "게시판 정보를 찾을 수 없습니다."),
  POST_NOT_FOUND(HttpStatus.NOT_FOUND, "BOARD_NOT_FOUND", "게시글 정보를 찾을 수 없습니다.")
  ;
  private final HttpStatus httpStatus;
  private final String statusCode;
  private final String description;
}
