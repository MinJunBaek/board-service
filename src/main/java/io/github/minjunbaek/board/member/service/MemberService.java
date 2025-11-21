package io.github.minjunbaek.board.member.service;

import io.github.minjunbaek.board.common.error.MemberErrorCode;
import io.github.minjunbaek.board.common.exception.ApiException;
import io.github.minjunbaek.board.member.controller.dto.MemberInformationDto;
import io.github.minjunbaek.board.member.controller.dto.MemberRegisterDto;
import io.github.minjunbaek.board.member.repository.MemberRepository;
import io.github.minjunbaek.board.member.repository.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService{

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;

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
  public MemberInformationDto getMyInformation(String email) {
    Member member = memberRepository.findByEmail(email)
        .orElseThrow(() -> new ApiException(MemberErrorCode.MEMBER_NOT_FOUND));

    return MemberInformationDto.of(member.getEmail(), member.getName(), member.getAddress(),
        member.getMemberRole().toString());
  }

}
