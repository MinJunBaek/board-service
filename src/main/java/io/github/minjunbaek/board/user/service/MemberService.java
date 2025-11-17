package io.github.minjunbaek.board.user.service;

import io.github.minjunbaek.board.user.controller.dto.LoginUserSessionDto;
import io.github.minjunbaek.board.user.controller.dto.MemberLoginRequestDto;
import io.github.minjunbaek.board.user.controller.dto.MemberRegisterDto;
import io.github.minjunbaek.board.user.repository.MemberRepository;
import io.github.minjunbaek.board.user.repository.entity.Member;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public String register(MemberRegisterDto memberRegisterDto) {
    if (memberRepository.existsByEmail(memberRegisterDto.getEmail())) {
      throw new IllegalArgumentException("이미 사용 중인 이메일 입니다.");
    }

    Member newMember = Member.of(memberRegisterDto.getEmail(), passwordEncoder.encode(memberRegisterDto.getPassword()),
        memberRegisterDto.getName(), memberRegisterDto.getAddress());

    memberRepository.save(newMember);
    return "저장완료";
  }

  @Transactional(readOnly = true)
  public String memberLogin(MemberLoginRequestDto memberLoginRequestDto, HttpSession httpSession) {
    Member member = memberRepository.findByEmail(memberLoginRequestDto.getEmail())
        .orElseThrow(() -> new UsernameNotFoundException(memberLoginRequestDto.getEmail() + "아이디 혹은 비밀번호가 잘못되었습니다."));

    if (!passwordEncoder.matches(memberLoginRequestDto.getPassword(), member.getPassword())) {
      throw new UsernameNotFoundException("아이디 혹은 비밀번호가 잘못되었습니다.");
    }

    LoginUserSessionDto sessionDto = LoginUserSessionDto.of(member.getId(), member.getEmail(), member.getName());

    httpSession.setAttribute("loginUser", sessionDto);

    return "로그인완료";
  }
}
