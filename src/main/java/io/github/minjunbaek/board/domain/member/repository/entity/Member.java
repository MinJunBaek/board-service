package io.github.minjunbaek.board.domain.member.repository.entity;

import io.github.minjunbaek.board.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity(name = "members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE members SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
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

  public void changePassword(String password) {
    this.password = password;
  }

  public void changeName(String name) {
    this.name = name;
  }

  public void changeAddress(String address) {
    this.address = address;
  }
}
