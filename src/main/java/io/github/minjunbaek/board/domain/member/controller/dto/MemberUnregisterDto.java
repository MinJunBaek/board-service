package io.github.minjunbaek.board.domain.member.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class MemberUnregisterDto {

  @NotBlank(message = "필수 입력값입니다.")
  private String password;
}
