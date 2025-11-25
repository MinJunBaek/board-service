package io.github.minjunbaek.board.web;

import io.github.minjunbaek.board.domain.member.controller.dto.MemberRegisterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberPageController {

  // 회원 가입 페이지
  @GetMapping("/join-form")
  public String joinForm(Model model) {
    model.addAttribute("memberRegisterDto", new MemberRegisterDto());
    return "join-form";
  }

  // 회원 탈퇴 페이지

  // 내정보 조회 페이지

  // 내정보 수정 페이지
}
