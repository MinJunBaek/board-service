package io.github.minjunbaek.board.domain.member.controller.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.springframework.util.StringUtils;

@Getter
public class EditInformationRequestDto {

  @Size(min = 4, message = "4자 이상입력해야 합니다.")
  @NotBlank(message = "비밀번호 입력은 필수입니다.")
  private String password;

  @Pattern(regexp = "^(|\\S{4,})$", message = "새 비밀번호는 비워두거나 4자 이상이어야 합니다.")
  private String changePassword;

  @Pattern(regexp = "^(|\\S{4,})$", message = "새 비밀번호 확인은 비워두거나 4자 이상이어야 합니다.")
  private String confirmPasswordChange;

  @Size(max = 50, message = "이름은 최대 50자 이하입니다.")
  private String name;

  @Size(max = 100, message = "주소는 최대 100자 이하입니다.")
  private String address;

  // 새 비밀번호와 새 비밀번호 확인이 일치하는지 확인
  @AssertTrue(message = "새 비밀번호와 새 비밀번호 확인이 일치하지 않습니다.")
  private boolean isPasswordConfirmMatched() {
    boolean hasChangePassword = StringUtils.hasText(changePassword);
    boolean hasConfirmChangePassword = StringUtils.hasText(confirmPasswordChange);

    if (!hasChangePassword && !hasConfirmChangePassword) {
      return true;
    }
    boolean result = changePassword.equals(confirmPasswordChange);
    return result;
  }

  @AssertTrue(message = "새 비밀번호는 현재 비밀번호와 달라야 합니다.")
  private boolean isNewPasswordDifferentFromCurrent() {
    if (!StringUtils.hasText(changePassword)){
      return true;
    }
    if (!StringUtils.hasText(password)) {
      return true;
    }
    return !password.equals(changePassword);
  }
}
