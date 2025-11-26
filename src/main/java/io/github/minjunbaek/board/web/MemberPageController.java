package io.github.minjunbaek.board.web;

import io.github.minjunbaek.board.domain.member.controller.dto.MemberRegisterDto;
import io.github.minjunbaek.board.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberPageController {

  private final MemberService memberService;

  // 로그인 페이지
  @GetMapping("/login")
  public String loginPage() {
    return "login-form"; // templates/login-form.html
  }

  // 로그아웃 페이지

  // 회원 가입 페이지로 이동
  @GetMapping("/join-form")
  public String joinForm(Model model) {
    model.addAttribute("register", new MemberRegisterDto());
    return "join-form";
  }

  // 회원 가입
  @PostMapping("/join")
  public String joinForm(
      @Validated @ModelAttribute("register") MemberRegisterDto memberRegisterDto,
      BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return "join-form";
    }

    memberService.register(memberRegisterDto);

    return "redirect:/"; // 추후 login-form으로 이동하자
  }

  // 회원 탈퇴 페이지

  // 내정보 조회 페이지

  // 내정보 수정 페이지
}
