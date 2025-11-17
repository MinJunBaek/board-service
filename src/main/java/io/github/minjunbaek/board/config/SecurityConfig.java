package io.github.minjunbaek.board.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
    // 세션 기반
    httpSecurity
      .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
      // 요청 인가
      .authorizeHttpRequests(auth -> auth
          // Swagger(OpenAPI) & Scalar
          .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**", "/scalar/**").permitAll()
          // H2 콘솔
          .requestMatchers("/h2-console/**").permitAll()
          // 메인 페이지 익명 허용
          .requestMatchers("/").permitAll()
          // 회원가입은 익명 허용
          .requestMatchers("/members/join-form", "/members/register").permitAll()
          .requestMatchers("/members/login").permitAll()
          // 그 외는 인증
          .anyRequest().authenticated()
      )

      // 기본 로그인/로그아웃 폼 사용
      .formLogin(Customizer.withDefaults())
      .logout(Customizer.withDefaults())

      // H2 콘솔용 헤더/CSRF 예외
      .headers(h -> h.frameOptions(frame -> frame.disable()))
      .csrf(csrf -> csrf.disable());

    return httpSecurity.build();
  }
}
