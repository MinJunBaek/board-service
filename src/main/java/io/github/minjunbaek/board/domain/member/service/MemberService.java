package io.github.minjunbaek.board.domain.member.service;

import static org.springframework.util.StringUtils.hasText;

import io.github.minjunbaek.board.common.error.MemberErrorCode;
import io.github.minjunbaek.board.common.exception.ApiException;
import io.github.minjunbaek.board.domain.member.controller.dto.EditInformationRequestDto;
import io.github.minjunbaek.board.domain.member.controller.dto.MemberInformationDto;
import io.github.minjunbaek.board.domain.member.controller.dto.MemberRegisterDto;
import io.github.minjunbaek.board.domain.member.controller.dto.MemberUnregisterDto;
import io.github.minjunbaek.board.domain.member.repository.MemberRepository;
import io.github.minjunbaek.board.domain.member.repository.entity.Member;
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

    duplicateEmailCheck(memberRegisterDto.getEmail());

    Member newMember = Member.of(memberRegisterDto.getEmail(), passwordEncoder.encode(memberRegisterDto.getPassword()),
        memberRegisterDto.getName(), memberRegisterDto.getAddress());

    memberRepository.save(newMember);
    return "저장완료";
  }

  @Transactional
  public String unregister(Long memberId, MemberUnregisterDto memberUnregisterDto) {
    Member member = findMember(memberId);

    if (!passwordEncoder.matches(memberUnregisterDto.getPassword(), member.getPassword())) {
      throw new ApiException(MemberErrorCode.LOGIN_FAILED);
    }

    memberRepository.delete(member);
    return "탈퇴 성공";
  }

  @Transactional(readOnly = true)
  public MemberInformationDto getMyInformation(Long memberId) {
    Member member = findMember(memberId);

    return MemberInformationDto.of(member.getEmail(), member.getName(), member.getAddress(),
        member.getMemberRole().getRole());
  }

  @Transactional
  public MemberInformationDto editMyInformation(Long id, EditInformationRequestDto requestDto) {
    Member member = findMember(id);

    // 현재 비밀번호 검증
    if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
      throw new ApiException(MemberErrorCode.LOGIN_FAILED);
    }

    // 현재 비밀번호를 새 비밀번호로 교체
    if (hasText(requestDto.getChangePassword())){
      String changePassword = passwordEncoder.encode(requestDto.getChangePassword());
      member.changePassword(changePassword);
    }

    // 변경할 이름이 있으면 변경
    if (hasText(requestDto.getName())) {
      member.changeName(requestDto.getName());
    }

    // 변경할 주소가 있으면 변경
    if (hasText(requestDto.getAddress())) {
      member.changeAddress(requestDto.getAddress());
    }

    // 결과 - Dto 로 변환하는 코드가 2번 중복되어 따로 메서드로 분리할수 있지만 굳이 하지 않음.
    return MemberInformationDto.of(member.getEmail(), member.getName(), member.getAddress(),
        member.getMemberRole().getRole());
  }

  public Member findMember(Long memberId) {
    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new ApiException(MemberErrorCode.MEMBER_NOT_FOUND));
    return member;
  }

  // 이메일 중복 체크
  private void duplicateEmailCheck(String email) {
    if (memberRepository.existsByEmail(email)) {
      throw new ApiException(MemberErrorCode.EMAIL_DUPLICATED);
    }

    if (memberRepository.existsByEmailAndDeletedAtIsNotNull(email)) {
      throw new ApiException(MemberErrorCode.EMAIL_WITHDRAWN);
    }
  }
}
