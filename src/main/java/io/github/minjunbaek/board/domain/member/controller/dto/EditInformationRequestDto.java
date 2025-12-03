package io.github.minjunbaek.board.domain.member.controller.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

@Getter
@Setter
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
  @AssertTrue(message = "새 비밀번호와 새 비밀번호 확인이 일치하지 않습니다.") // 이 메서드의 결과가 True 여야 유효
  public boolean isPasswordConfirmMatched() {
    boolean hasChangePassword = StringUtils.hasText(changePassword);
    boolean hasConfirmChangePassword = StringUtils.hasText(confirmPasswordChange);

    // 둘다 값이 비어있을 경우 비밀번호 변경을 안하는 것으로 true로 통과
    if (!hasChangePassword && !hasConfirmChangePassword) {
      return true;
    }

    // 값이 한쪽만 입력되어있는 경우 false를 반환하여 message 출력
    if (hasChangePassword != hasConfirmChangePassword) {
      return false;
    }

    boolean result = changePassword.equals(confirmPasswordChange);
    return result;
  }

  @AssertTrue(message = "새 비밀번호는 현재 비밀번호와 달라야 합니다.") // 이 메서드의 결과가 True 여야 유효
  public boolean isNewPasswordDifferentFromCurrent() {
    if (!StringUtils.hasText(password) && !StringUtils.hasText(changePassword)){ // 현재 비밀번호와 새 비밀번호가 입력이 되었는지 확인
      return true;
    }
    return !password.equals(changePassword);
  }
}
