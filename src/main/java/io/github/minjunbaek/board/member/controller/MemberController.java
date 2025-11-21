package io.github.minjunbaek.board.member.controller;

import io.github.minjunbaek.board.common.api.Api;
import io.github.minjunbaek.board.common.error.MemberErrorCode;
import io.github.minjunbaek.board.common.exception.ApiException;
import io.github.minjunbaek.board.member.controller.dto.LoginUserSessionDto;
import io.github.minjunbaek.board.member.controller.dto.MemberLoginRequestDto;
import io.github.minjunbaek.board.member.controller.dto.MemberRegisterDto;
import io.github.minjunbaek.board.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

  // 로그인
  @PostMapping("/login")
  public ResponseEntity<Api<LoginUserSessionDto>> memberLogin(@RequestBody @Validated MemberLoginRequestDto memberLoginRequestDto, HttpSession httpSession) {
    LoginUserSessionDto result = memberService.memberLogin(memberLoginRequestDto, httpSession);
    return ResponseEntity.ok(Api.success("로그인 성공", "사용자 로그인 성공", result));
  }

  // 아이디 및 패스워드 찾기

  // 로그아웃
  // 내정보 조회
  // 내정보 수정
  // 내 비밀번호 수정
  // 회원 탈퇴

  // 현재 로그인된 유저 확인용 API
  @GetMapping("/me")
  public ResponseEntity<Api<LoginUserSessionDto>> getCurrentMember(
      @AuthenticationPrincipal UserDetails userDetails,
      HttpSession httpSession) {
    String userEmail = userDetails.getUsername();
    LoginUserSessionDto loginUser = (LoginUserSessionDto) httpSession.getAttribute("loginUser");

    if (loginUser == null) {
      throw new ApiException(MemberErrorCode.MEMBER_NOT_FOUND);
    }

    Api<LoginUserSessionDto> body = Api.success("ME", "현재 로그인된 사용자입니다.", loginUser);

    return ResponseEntity.ok(body);
  }
}
