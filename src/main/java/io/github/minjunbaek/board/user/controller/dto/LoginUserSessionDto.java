package io.github.minjunbaek.board.user.controller.dto;

public class LoginUserSessionDto {
  private Long id;
  private String email;
  private String name;

  public static LoginUserSessionDto of(Long id, String email, String name) {
    LoginUserSessionDto loginUserSessionDto = new LoginUserSessionDto();
    loginUserSessionDto.id = id;
    loginUserSessionDto.email = email;
    loginUserSessionDto.name = name;
    return loginUserSessionDto;
  }
}
