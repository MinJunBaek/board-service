package io.github.minjunbaek.board.user.repository.entity;

import io.github.minjunbaek.board.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "member")
@Getter
@NoArgsConstructor
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
}
