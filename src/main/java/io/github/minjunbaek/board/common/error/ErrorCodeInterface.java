package io.github.minjunbaek.board.common.error;

import org.springframework.http.HttpStatus;

public interface ErrorCodeInterface {
  HttpStatus getHttpStatus();
  String getStatusCode();
  String getDescription();
}
