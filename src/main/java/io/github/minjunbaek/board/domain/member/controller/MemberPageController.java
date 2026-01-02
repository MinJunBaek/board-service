package io.github.minjunbaek.board.domain.member.controller;

import io.github.minjunbaek.board.common.error.MemberErrorCode;
import io.github.minjunbaek.board.common.exception.ApiException;
import io.github.minjunbaek.board.domain.board.service.BoardService;
import io.github.minjunbaek.board.domain.member.controller.dto.EditInformationRequestDto;
import io.github.minjunbaek.board.domain.member.controller.dto.MemberInformationDto;
import io.github.minjunbaek.board.domain.member.controller.dto.MemberLoginRequestDto;
import io.github.minjunbaek.board.domain.member.controller.dto.MemberRegisterDto;
import io.github.minjunbaek.board.domain.member.controller.dto.MemberUnregisterDto;
import io.github.minjunbaek.board.domain.member.service.MemberService;
import io.github.minjunbaek.board.domain.post.controller.dto.PostListResponseDto;
import io.github.minjunbaek.board.domain.post.service.PostService;
import io.github.minjunbaek.board.security.MemberPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberPageController {

  private final MemberService memberService;
  private final BoardService boardService;
  private final PostService postService;

  // 로그인 페이지로 이동
  @GetMapping("/login-form")
  public String loginPage(@RequestParam(value = "error", required = false) String error, Model model
  ) {
    // 로그인 폼 객체 ==> 이것을 삭제하면 localhost에서 리디렉션한 횟수가 너무 많습니다. 라는 내용이 나옴 이유가 뭘까?
    if (!model.containsAttribute("loginForm")) {
      model.addAttribute("loginForm", new MemberLoginRequestDto());
    }

    // 로그인 실패 시 에러 플래그(스프링 시큐리티에서 ?error 붙여줄 예정)
    if (error != null) {
      model.addAttribute("loginError", true);
    }

    return "members/login-form";
  }

  // 회원 가입 페이지로 이동
  @GetMapping("/join-form")
  public String joinForm(Model model) {
    model.addAttribute("register", new MemberRegisterDto());
    return "members/join-form";
  }

  // 회원 가입
  @PostMapping("/join")
  public String joinForm(
      @Validated @ModelAttribute("register") MemberRegisterDto memberRegisterDto,
      BindingResult bindingResult, RedirectAttributes redirectAttributes) {

    // 폼 검즘 시 에러가 있다면 그대로 회원가입 폼으로
    if (bindingResult.hasErrors()) {
      return "members/join-form";
    }

    try {
      memberService.register(memberRegisterDto);
    } catch (ApiException e) {
      // 에러코드에 따라 글로벌 에러 추가
      bindingResult.reject(e.getErrorCodeInterface().getStatusCode(), e.getErrorCodeInterface().getDescription());
      return "members/join-form";
    }

    // 회원가입 성공
    redirectAttributes.addFlashAttribute(
        "joinSuccessMessage",
        "회원가입이 완료되었습니다. 로그인 해 주세요."
    );

    return "redirect:/members/login-form"; // 추후 login-form으로 이동하자
  }

  // 내정보 조회 및 수정 페이지로 이동
  @GetMapping("/me")
  public String viewMyInformation(
      @AuthenticationPrincipal MemberPrincipal memberPrincipal,
      Model model
  ) {

    Long memberId = memberPrincipal.getId();
    MemberInformationDto member = memberService.getMyInformation(memberId);
    model.addAttribute("member", member);

    EditInformationRequestDto editForm = new EditInformationRequestDto();
    editForm.setName(member.getName());
    editForm.setAddress(member.getAddress());
    model.addAttribute("editForm", editForm);

    return "members/my-information-form";
  }

  // 내정보 수정
  @PostMapping("/me")
  public String editMyInformation(
      @AuthenticationPrincipal MemberPrincipal memberPrincipal,
      @Validated @ModelAttribute("editForm") EditInformationRequestDto editForm,
      BindingResult bindingResult, Model model) {

    // 현재 내 정보 (이메일, 권한, 등)
    Long memberId = memberPrincipal.getId();
    MemberInformationDto member = memberService.getMyInformation(memberId);
    model.addAttribute("member", member);

    // 1) DTO 검증(빈 비밀번호, 새 비밀번호 일치 여부 등)
    if (bindingResult.hasErrors()) {
      return "members/my-information-form";
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
      return "members/my-information-form";
    }
    return "members/my-information-form";
  }

  @GetMapping("/me/posts")
  public String viewMyPostList(
      @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
      @AuthenticationPrincipal MemberPrincipal memberPrincipal, Model model) {

    Long memberId = memberPrincipal.getId();
    Page<PostListResponseDto> postsPage = postService.readAllMemberPost(pageable, memberId);
    model.addAttribute("postsPage", postsPage);

    return "members/my-posts-form";
  }

  @GetMapping("/me/withdraw")
  public String unregisterPage(@AuthenticationPrincipal MemberPrincipal memberPrincipal, Model model) {

    model.addAttribute("memberPrincipalEmail", memberPrincipal.getEmail());

    MemberUnregisterDto unregisterDto = new MemberUnregisterDto();
    model.addAttribute("unregister", unregisterDto);

    return "members/my-unregister-form";
  }

  // 회원 탈퇴 POST (폼 제출)
  @PostMapping("/me/withdraw")
  public String unregisterMemberPost(
      @AuthenticationPrincipal MemberPrincipal memberPrincipal,
      @Validated @ModelAttribute("unregister") MemberUnregisterDto unregisterDto,
      BindingResult bindingResult,
      Model model,
      HttpServletRequest request,
      HttpServletResponse response
  ) {

    model.addAttribute("memberPrincipalEmail", memberPrincipal.getEmail());

    if (bindingResult.hasErrors()) {
      // 비밀번호 NotBlank 같은 DTO 검증 에러
      return "members/my-unregister-form";
    }

    try {
      Long memberId = memberPrincipal.getId();
      String result = memberService.unregister(memberId, unregisterDto);

      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth != null) {
        new SecurityContextLogoutHandler().logout(request, response, auth);
      }

      // 탈퇴 성공 후 로그인 페이지나 메인으로 리다이렉트
      return "redirect:/members/login-form?unregisterSuccess";

    } catch (ApiException e) {
      // 비밀번호 불일치 같은 비즈니스 에러
      if (e.getErrorCodeInterface() == MemberErrorCode.LOGIN_FAILED) {
        bindingResult.rejectValue("password", "invalidPassword", "비밀번호가 일치하지 않습니다.");
      } else {
        bindingResult.reject("unregisterFailed", e.getMessage());
      }
      return "members/my-unregister-form";
    }
  }
}
