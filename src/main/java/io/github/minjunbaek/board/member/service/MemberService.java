package io.github.minjunbaek.board.member.service;

import io.github.minjunbaek.board.common.error.MemberErrorCode;
import io.github.minjunbaek.board.common.exception.ApiException;
import io.github.minjunbaek.board.member.controller.dto.LoginUserSessionDto;
import io.github.minjunbaek.board.member.controller.dto.MemberLoginRequestDto;
import io.github.minjunbaek.board.member.controller.dto.MemberRegisterDto;
import io.github.minjunbaek.board.member.repository.MemberRepository;
import io.github.minjunbaek.board.member.repository.entity.Member;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Member member = getMemberByEmailOrThrow(username);
    return member;
  }

  @Transactional
  public String register(MemberRegisterDto memberRegisterDto) {
    if (memberRepository.existsByEmail(memberRegisterDto.getEmail())) {
      throw new ApiException(MemberErrorCode.EMAIL_DUPLICATED);
    }

    Member newMember = Member.of(memberRegisterDto.getEmail(), passwordEncoder.encode(memberRegisterDto.getPassword()),
        memberRegisterDto.getName(), memberRegisterDto.getAddress());

    memberRepository.save(newMember);
    return "저장완료";
  }

  @Transactional(readOnly = true)
  public LoginUserSessionDto memberLogin(MemberLoginRequestDto memberLoginRequestDto, HttpSession httpSession) {
    Member member = getMemberByEmailOrThrow(memberLoginRequestDto.getEmail());

    if (!passwordEncoder.matches(memberLoginRequestDto.getPassword(), member.getPassword())) {
      throw new ApiException(MemberErrorCode.LOGIN_FAILED);
    }

    LoginUserSessionDto sessionDto = LoginUserSessionDto.of(member.getId(), member.getEmail(), member.getName());

    httpSession.setAttribute("loginUser", sessionDto);

    return sessionDto;
  }

  private Member getMemberByEmailOrThrow(String email) {
    Member member = memberRepository.findByEmail(email)
        .orElseThrow(() -> new ApiException(MemberErrorCode.LOGIN_FAILED));
    return member;
  }
}
