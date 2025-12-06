package io.github.minjunbaek.board.domain.member.repository.entity;

import lombok.Getter;

public enum MemberRole {
  ADMIN("관리자", 0),
  MEMBER("회원", 1),
  ;

  @Getter
  private final String role;
  private final Integer number;

  MemberRole(String role, Integer number) {
    this.role = role;
    this.number = number;
  }

  public String toString() {
    return "ROLE_" + this.name();
  }
}
