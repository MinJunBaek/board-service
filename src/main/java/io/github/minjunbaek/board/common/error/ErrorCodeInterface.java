package io.github.minjunbaek.board.common.error;

import org.springframework.http.HttpStatus;

// 에러코드 전용 인터페이스로 사용할게 아니라 성공 Enum 클레스도 만들어서 반환하는 역할을 맡는 인터페이스로 사용할까?
public interface ErrorCodeInterface {
  HttpStatus getHttpStatus();
  String getStatusCode();
  String getDescription();
}
