package io.github.minjunbaek.board.user.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class MemberRegisterDto {

  @Email
  @NotBlank(message = "이메일 입력은 필수입니다.")
  private String email;

  @Size(min = 8, message = "비밀번호 입력은 필수입니다.")
  private String password;

  @NotEmpty
  private String name;

  @NotEmpty
  private String address;
}
