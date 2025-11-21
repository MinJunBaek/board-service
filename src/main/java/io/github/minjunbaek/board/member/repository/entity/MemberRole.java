package io.github.minjunbaek.board.member.repository.entity;

public enum MemberRole {
  ADMIN("관리자", 0),
  MEMBER("회원", 1),
  ;

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
