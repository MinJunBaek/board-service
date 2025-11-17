package io.github.minjunbaek.board.user.controller;

import io.github.minjunbaek.board.user.controller.dto.MemberLoginRequestDto;
import io.github.minjunbaek.board.user.controller.dto.MemberRegisterDto;
import io.github.minjunbaek.board.user.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
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
  public ResponseEntity<String> register(@RequestBody @Validated MemberRegisterDto memberRegisterDto) {
    String result = memberService.register(memberRegisterDto);
    return ResponseEntity.status(201).body(result);
  }

  // 로그인
  @PostMapping("/login")
  public ResponseEntity<String> memberLogin(@RequestBody @Validated MemberLoginRequestDto memberLoginRequestDto, HttpSession httpSession) {
    String result = memberService.memberLogin(memberLoginRequestDto, httpSession);
    return ResponseEntity.ok(result);
  }
}
