package io.github.minjunbaek.board.exceptionhandler;

import io.github.minjunbaek.board.common.api.Api;
import io.github.minjunbaek.board.common.error.CommonErrorCode;
import io.github.minjunbaek.board.common.error.ErrorCodeInterface;
import io.github.minjunbaek.board.common.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class GlobalExceptionHandler {

  /**
   * 비즈니스 로직에서 명시적으로 던지는 ApiException 처리
   */
  @ExceptionHandler(ApiException.class)
  public ResponseEntity<Api<Void>> handleApiException(ApiException e) {
    ErrorCodeInterface errorCode = e.getErrorCodeInterface();
    log.warn("ApiException 발생: statusCode={}, description={}",
        errorCode.getStatusCode(), errorCode.getDescription());

    return ResponseEntity.status(errorCode.getHttpStatus())
        .body(Api.failure(errorCode.getStatusCode(), errorCode.getDescription()));
  }

  /**
   * @Valid 검증 실패 등의 예외 처리
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Api<Void>> handleValidationException(MethodArgumentNotValidException e) {
    ErrorCodeInterface errorCode = CommonErrorCode.INVALID_INPUT_VALUE;

    String detailMessage = e.getBindingResult().getFieldErrors().stream()
        .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
        .findFirst()
        .orElse(errorCode.getDescription());

    return ResponseEntity.status(errorCode.getHttpStatus()).body(Api.failure(errorCode.getStatusCode(),
        detailMessage));
  }
  /**
   * 그 밖의 예상하지 못한 예외 처리
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Api<Void>> handleException(Exception e) {
    log.error("예상하지 못한 예외 발생", e);

    ErrorCodeInterface errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;

    return ResponseEntity
        .status(errorCode.getHttpStatus())
        .body(Api.failure(errorCode.getStatusCode(), errorCode.getDescription()));
  }
}
