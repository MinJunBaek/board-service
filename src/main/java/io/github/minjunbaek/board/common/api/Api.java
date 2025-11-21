package io.github.minjunbaek.board.common.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class Api<T> {

  private final boolean success; // 성공 여부(성공 true, 실패 false)
  private final String statusCode; // 도메인 상태 표시
  private final String description; // 상태 메시지
  private final T data; // 데이터

  public static Api<Void> success(String statusCode, String description) {
    return Api.of(true, statusCode, description, null);
  }

  public static <T> Api<T> success(String statusCode, String description, T data) {
    return Api.of(true, statusCode, description, data);
  }

  public static Api<Void> failure(String statusCode, String description) {
    return Api.of(false, statusCode, description, null);
  }

  public static <T> Api<T> failure(String statusCode, String description, T data) {
    return Api.of(false, statusCode, description, data);
  }
}
