package io.github.minjunbaek.board.member.controller;

import io.github.minjunbaek.board.member.controller.dto.MemberRegisterDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/members")
public class MemberPageController {

  @GetMapping(value = "/join-form")
  public String joinForm(Model model) {
    model.addAttribute("memberRegisterDto", new MemberRegisterDto());
    return "member/JoinForm";
  }
}
