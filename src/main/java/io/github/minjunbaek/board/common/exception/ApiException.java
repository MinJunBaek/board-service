package io.github.minjunbaek.board.common.exception;

import io.github.minjunbaek.board.common.error.ErrorCodeInterface;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException implements ApiExceptionInterface {

  private final ErrorCodeInterface errorCodeInterface;

  public ApiException(ErrorCodeInterface errorCodeInterface) {
    super(errorCodeInterface.getDescription());
    this.errorCodeInterface = errorCodeInterface;
  }
}
