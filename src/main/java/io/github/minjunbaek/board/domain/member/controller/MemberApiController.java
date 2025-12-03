package io.github.minjunbaek.board.domain.member.controller;

import io.github.minjunbaek.board.common.api.Api;
import io.github.minjunbaek.board.common.error.MemberErrorCode;
import io.github.minjunbaek.board.common.exception.ApiException;
import io.github.minjunbaek.board.domain.member.controller.dto.EditInformationRequestDto;
import io.github.minjunbaek.board.domain.member.controller.dto.MemberInformationDto;
import io.github.minjunbaek.board.domain.member.controller.dto.MemberRegisterDto;
import io.github.minjunbaek.board.domain.member.controller.dto.MemberUnregisterDto;
import io.github.minjunbaek.board.domain.member.service.MemberService;
import io.github.minjunbaek.board.domain.post.controller.dto.PostListResponseDto;
import io.github.minjunbaek.board.domain.post.service.PostService;
import io.github.minjunbaek.board.security.MemberPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberApiController {

  private final MemberService memberService;
  private final PostService postService;

  // 회원 가입
  @PostMapping
  public ResponseEntity<Api<Void>> register(@RequestBody @Validated MemberRegisterDto memberRegisterDto) {
    String result = memberService.register(memberRegisterDto);
    return ResponseEntity.status(201).body(Api.success("MEMBER_CREATED", result));
  }

  // 회원 탈퇴
  @PostMapping("/me")
  public ResponseEntity<Api<Void>> unregister(
      @AuthenticationPrincipal MemberPrincipal memberPrincipal,
      @Validated @RequestBody MemberUnregisterDto memberUnregisterDto,
      HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse) {

    Long memberId = memberPrincipal.getId();
    String result = memberService.unregister(memberId, memberUnregisterDto);

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null) {
      new SecurityContextLogoutHandler().logout(httpServletRequest,httpServletResponse, auth);
    }

    return ResponseEntity.ok(Api.success("MEMBER_DELETED", result));
  }

  // 내정보 조회
  @GetMapping("/me")
  public ResponseEntity<Api<MemberInformationDto>> viewMyInformation(
      @AuthenticationPrincipal MemberPrincipal memberPrincipal) {

    MemberInformationDto informationDto = memberService.getMyInformation(memberPrincipal.getId());

    return ResponseEntity.ok(Api.success("MY_INFORMATION", "내정보 조회", informationDto));
  }

  // 내정보 수정
  @PatchMapping("/me")
  public ResponseEntity<Api<MemberInformationDto>> editMyInformation(
      @AuthenticationPrincipal MemberPrincipal memberPrincipal,
      @Validated @RequestBody EditInformationRequestDto requestDto) {

    Long memberId = memberPrincipal.getId();

    MemberInformationDto informationDto = memberService.editMyInformation(memberId, requestDto);

    return ResponseEntity.ok(Api.success("MY_INFORMATION_CHANGE", "내정보 수정", informationDto));
  }

  // 내가 쓴 게시글 목록 조회
  @GetMapping("/me/posts")
  public ResponseEntity<Api<List<PostListResponseDto>>> viewMemberPosts(
      @AuthenticationPrincipal MemberPrincipal memberPrincipal
  ) {

    List<PostListResponseDto> postListResponseDto = postService.readAllMemberPost(memberPrincipal.getId());

    return ResponseEntity.ok(Api.success("VIEW_MY_POST_LIST", "사용자 게시글 목록 조회", postListResponseDto));
  }

  // 아이디 및 패스워드 찾기
}
