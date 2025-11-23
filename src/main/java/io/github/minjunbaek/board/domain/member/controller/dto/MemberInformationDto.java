package io.github.minjunbaek.board.domain.member.controller.dto;

import lombok.Getter;

@Getter
public class MemberInformationDto {

  private String email;

  private String name;

  private String address;

  private String memberRole;

  public static MemberInformationDto of (String email, String name, String address, String memberRole) {
    MemberInformationDto dto = new MemberInformationDto();
    dto.email = email;
    dto.name = name;
    dto.address = address;
    dto.memberRole = memberRole;
    return dto;
  }
}
