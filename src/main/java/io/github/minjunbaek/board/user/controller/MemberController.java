package io.github.minjunbaek.board.user.controller;

import io.github.minjunbaek.board.user.controller.dto.MemberRegisterDto;
import io.github.minjunbaek.board.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
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
  public String register(@RequestBody @Validated MemberRegisterDto memberRegisterDto) {
    String result = memberService.register(memberRegisterDto);
    return result;
  }
}
