package io.github.minjunbaek.board.common.error;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
  HttpStatus getHttpStatus();
  String getStatusCode();
  String getDescription();
}
