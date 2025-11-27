package io.github.minjunbaek.board.web;

import io.github.minjunbaek.board.common.error.MemberErrorCode;
import io.github.minjunbaek.board.common.exception.ApiException;
import io.github.minjunbaek.board.domain.board.controller.dto.BoardResponseDto;
import io.github.minjunbaek.board.domain.board.service.BoardService;
import io.github.minjunbaek.board.domain.member.controller.dto.EditInformationRequestDto;
import io.github.minjunbaek.board.domain.member.controller.dto.MemberInformationDto;
import io.github.minjunbaek.board.domain.member.controller.dto.MemberRegisterDto;
import io.github.minjunbaek.board.domain.member.service.MemberService;
import io.github.minjunbaek.board.security.MemberPrincipal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
  private final BoardService boardService;

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

  // 내정보 조회 및 수정 페이지로 이동
  @GetMapping("/me")
  public String viewMyInformation(
      @AuthenticationPrincipal MemberPrincipal memberPrincipal,
      Model model
  ) {

    // 게시판 목록 조회 (API에서 쓰던 로직 그대로 활용)
    List<BoardResponseDto> boards = boardService.readAllBoard();
    model.addAttribute("boards", boards);

    // 로그인 상태정보 확인
    if (memberPrincipal == null) {
      model.addAttribute("loggedIn", false);
      return "redirect:/login";
    }

    model.addAttribute("loggedIn", true);
    model.addAttribute("memberPrincipalName", memberPrincipal.getName());

    String memberEmail = memberPrincipal.getEmail();
    MemberInformationDto member = memberService.getMyInformation(memberEmail);
    model.addAttribute("member", member);

    EditInformationRequestDto editForm = new EditInformationRequestDto();
    editForm.setName(member.getName());
    editForm.setAddress(member.getAddress());
    model.addAttribute("editForm", editForm);

    return "my-information-form";
  }

  // 내정보 수정
  @PostMapping("/me")
  public String editMyInformation(
      @AuthenticationPrincipal MemberPrincipal memberPrincipal,
      @Validated @ModelAttribute("editForm") EditInformationRequestDto editForm,
      BindingResult bindingResult, Model model) {

    // 게시판 목록 조회 (API에서 쓰던 로직 그대로 활용)
    List<BoardResponseDto> boards = boardService.readAllBoard();
    model.addAttribute("boards", boards);

    // 로그인 상태정보 확인
    if (memberPrincipal == null) {
      model.addAttribute("loggedIn", false);
      return "redirect:/login";
    }
    model.addAttribute("loggedIn", true);
    model.addAttribute("memberPrincipalName", memberPrincipal.getName());

    // 현재 내 정보 (이메일, 권한, 등)
    String memberEmail = memberPrincipal.getEmail();
    MemberInformationDto member = memberService.getMyInformation(memberEmail);
    model.addAttribute("member", member);

    // 1) DTO 검증(빈 비밀번호, 새 비밀번호 일치 여부 등)
    if (bindingResult.hasErrors()) {
      return "my-information-form";
    }

    // 2) 서비스 호출 (현재 비밀번호 검증 + 수정)
    try {
      MemberInformationDto updated = memberService.editMyInformation(memberPrincipal.getId(), editForm);
      model.addAttribute("member", updated); // 수정된 값 다시 반영
      model.addAttribute("successMessage", "내 정보가 수정되었습니다.");
    } catch (ApiException e) {
      // 예: 비밀번호 불일치일 때 LOGIN_FAILED
      if (e.getErrorCodeInterface() == MemberErrorCode.LOGIN_FAILED) {
        bindingResult.rejectValue("password", "invalidPassword", "비밀번호가 일치하지 않습니다.");
      } else {
        bindingResult.reject("editFailed", e.getMessage());
      }
      return "my-information-form";
    }
    return "my-information-form";
  }
}
