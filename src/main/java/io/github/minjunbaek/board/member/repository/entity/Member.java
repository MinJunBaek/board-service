package io.github.minjunbaek.board.member.repository.entity;

import io.github.minjunbaek.board.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.util.Collection;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity(name = "members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

  @Column(length = 100, nullable = false)
  private String email;

  @Column(length = 255, nullable = false)
  private String password;

  @Column(length = 50, nullable = false)
  private String name;

  @Column(length = 100, nullable = false)
  private String address;

  @Column(length = 50, nullable = false)
  @Enumerated(EnumType.STRING)
  private MemberRole memberRole;

  public static Member of(String email, String password, String name, String address) {
    Member member = new Member();
    member.email = email;
    member.password = password;
    member.name = name;
    member.address = address;
    member.memberRole = MemberRole.MEMBER;
    return member;
  }
}
