package io.github.minjunbaek.board.user.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class MemberLoginRequestDto {

  @Email
  private String email;

  @NotBlank
  private String password;

}
