package io.github.minjunbaek.board.user.service;

import io.github.minjunbaek.board.user.controller.dto.MemberRegisterDto;
import io.github.minjunbaek.board.user.repository.MemberRepository;
import io.github.minjunbaek.board.user.repository.entity.Member;
import lombok.RequiredArgsConstructor;
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
    Member newMember = Member.of(memberRegisterDto.getEmail(), passwordEncoder.encode(memberRegisterDto.getPassword()),
        memberRegisterDto.getName(), memberRegisterDto.getAddress());
    memberRepository.save(newMember);
    return "저장완료";
  }
}
