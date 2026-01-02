package io.github.minjunbaek.board.exceptionhandler;

import io.github.minjunbaek.board.common.api.Api;
import io.github.minjunbaek.board.common.error.CommonErrorCode;
import io.github.minjunbaek.board.common.error.ErrorCodeInterface;
import io.github.minjunbaek.board.common.exception.ApiException;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class GlobalExceptionHandler {

  private void logError(
      String type,
      ErrorCodeInterface errorCode,
      Object detail,
      Throwable e
  ) {
    log.warn("[{}] httpStatus={}, statusCode={}, description={}, detail={}",
        type,
        errorCode.getHttpStatus(),
        errorCode.getStatusCode(),
        errorCode.getDescription(),
        detail,
        e
    );
  }

  /**
   * 비즈니스 로직에서 명시적으로 던지는 ApiException 처리
   */
  @ExceptionHandler(ApiException.class)
  public ResponseEntity<Api<Void>> handleApiException(ApiException e) {
    ErrorCodeInterface errorCode = e.getErrorCodeInterface();

    logError("ApiException", errorCode, null, e);

    return ResponseEntity.status(errorCode.getHttpStatus())
        .body(Api.failure(errorCode.getStatusCode(), errorCode.getDescription()));
  }

  /**
   * @Valid / @Validated 바인딩 실패 (JSON 본문을 DTO 객체로 변환 후, 그 객체의 필드를 검증)
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Api<Map<String, String>>> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
    ErrorCodeInterface errorCode = CommonErrorCode.INVALID_INPUT_VALUE;
    Map<String, String> errors = new HashMap<>();

    for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
      errors.put(fieldError.getField(), fieldError.getDefaultMessage());
    }

    logError("MethodArgumentNotValidException", errorCode, errors, e);

    return ResponseEntity
        .status(errorCode.getHttpStatus())
        .body(Api.failure(
            errorCode.getStatusCode(),
            errorCode.getDescription(),
            errors
        ));
  }

  /**
   * RequestParam, PathVariable (요청의 파라미터나 경로 변수 값 자체를 직접 검증)
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Api<String>> handleConstraintViolation(ConstraintViolationException e) {
    ErrorCodeInterface errorCode = CommonErrorCode.INVALID_INPUT_VALUE;

    logError("ConstraintViolationException", errorCode, e.getMessage(), e);

    return ResponseEntity
        .status(errorCode.getHttpStatus())
        .body(Api.failure(
            errorCode.getStatusCode(),
            errorCode.getDescription(),
            e.getMessage()
        ));
  }

  /**
   * 그 밖의 예상하지 못한 예외 처리
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Api<Void>> handleException(Exception e) {
    log.error("[Exception] : message={}", e.getMessage(), e);

    ErrorCodeInterface errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;

    return ResponseEntity
        .status(errorCode.getHttpStatus())
        .body(Api.failure(errorCode.getStatusCode(), errorCode.getDescription()));
  }

}
