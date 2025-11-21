package io.github.minjunbaek.board.member.controller;

import io.github.minjunbaek.board.common.api.Api;
import io.github.minjunbaek.board.common.error.MemberErrorCode;
import io.github.minjunbaek.board.common.exception.ApiException;
import io.github.minjunbaek.board.member.controller.dto.LoginUserSessionDto;
import io.github.minjunbaek.board.member.controller.dto.MemberInformationDto;
import io.github.minjunbaek.board.member.controller.dto.MemberRegisterDto;
import io.github.minjunbaek.board.member.service.MemberService;
import io.github.minjunbaek.board.security.MemberPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

  private final MemberService memberService;

  // 회원가입
  @PostMapping("/register")
  public ResponseEntity<Api> register(@RequestBody @Validated MemberRegisterDto memberRegisterDto) {
    String result = memberService.register(memberRegisterDto);
    return ResponseEntity.status(201).body(Api.success("MEMBER_CREATED", result));
  }

  // 내정보 조회
  @GetMapping("/my-information")
  public ResponseEntity<Api<MemberInformationDto>> myInformation(
      @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
    if (memberPrincipal == null) {
      throw new ApiException(MemberErrorCode.MEMBER_NOT_FOUND);
    }

    MemberInformationDto informationDto = memberService.getMyInformation(memberPrincipal.getEmail());

    return ResponseEntity.ok(Api.success("MY_INFORMATION", "내정보 조회", informationDto));
  }
  // 내정보 수정
  // 아이디 및 패스워드 찾기
  // 내 비밀번호 수정
  // 회원 탈퇴

  // 현재 로그인된 유저 확인용 API
  @GetMapping("/me")
  public ResponseEntity<Api<LoginUserSessionDto>> getCurrentMember(
      @AuthenticationPrincipal MemberPrincipal principal) {

    if (principal == null) {
      throw new ApiException(MemberErrorCode.MEMBER_NOT_FOUND);
    }

    LoginUserSessionDto dto = LoginUserSessionDto.of(
        principal.getId(),
        principal.getEmail(),
        principal.getName()
    );

    Api<LoginUserSessionDto> body = Api.success("ME", "현재 로그인된 사용자입니다.", dto);

    return ResponseEntity.ok(body);
  }
}
