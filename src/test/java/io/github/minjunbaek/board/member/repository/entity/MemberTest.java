package io.github.minjunbaek.board.member.repository.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {

  @Test
  @DisplayName("회원객체 생성")
  void of() {
    // given
    String email = "member1@email.com";
    String password = "password1234";
    String name = "회원1";
    String address = "경기도 화성시";

    // when
    Member member = Member.of(email, password, name, address);

    // then
    Assertions.assertThat(member).isNotNull();
    Assertions.assertThat(member.getEmail()).isEqualTo(email);
    Assertions.assertThat(member.getPassword()).isEqualTo(password);
    Assertions.assertThat(member.getName()).isEqualTo(name);
    Assertions.assertThat(member.getAddress()).isEqualTo(address);
    Assertions.assertThat(member.getMemberRole()).isEqualTo(MemberRole.MEMBER);
  }
}