package io.github.minjunbaek.board.security;

import io.github.minjunbaek.board.domain.member.repository.entity.Member;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@RequiredArgsConstructor
public class MemberPrincipal implements UserDetails {

  private final Member member;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(member.getMemberRole().toString()));
  }

  public Long getId() {
    return member.getId();
  }

  public String getEmail() {
    return member.getEmail();
  }

  public String getName() {
    return member.getName();
  }

  @Override
  public String getPassword() {
    return member.getPassword();
  }

  @Override
  public String getUsername() {
    return member.getEmail();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }  // 계정 만료 여부

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }  // 계정 잠김 여부

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }  // 비밀번호 만료 여부

  @Override
  public boolean isEnabled() {
    return true;
  }  // 계정 활성화 여부
}
