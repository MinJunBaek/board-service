package io.github.minjunbaek.board.config;

import io.github.minjunbaek.board.security.MemberUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final MemberUserDetailsService memberUserDetailsService;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
    // 세션 기반
    httpSecurity
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)  // 필요할 때 세션 생성 (기본)
            .maximumSessions(1) // 동시 세션 수 제한(Optional)
        )

        // 요청 인가
        .authorizeHttpRequests(auth -> auth
            // Swagger(OpenAPI) & Scalar => 관리자 전용
            .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**", "/scalar/**").hasRole("ADMIN")
            // 모든 API 요청은 관리자만 가능 (댓글 수정은 제외)
            .requestMatchers(HttpMethod.PATCH, "/api/comments/**").authenticated()
            .requestMatchers("/api/**").hasRole("ADMIN")
            // 익명 허용(메인페이지, 회원가입)
            .requestMatchers("/", "/members/join-form", "/members/join").permitAll()
            // 익명 사용자의 게시글 목록 조회 허용
            .requestMatchers(HttpMethod.GET, "/boards/*/posts").permitAll()
            // 그 외는 인증
            .anyRequest().authenticated()
        )
        .userDetailsService(memberUserDetailsService)

        // 커스텀 로그인 폼 사용
        .formLogin(form -> form
            .loginPage("/members/login-form")     // 로그인 페이지 URL (GET)
            .loginProcessingUrl("/members/login") // 로그인 처리 URL (POST) - 컨트롤러 X
            .usernameParameter("email")           // 폼의 name="email"
            .passwordParameter("password")        // 폼의 name="password"
            .defaultSuccessUrl("/", true)         // 로그인 성공 후 이동
            .failureUrl("/members/login-form?error") // 실패 시 다시 로그인 페이지 + ?error
            .permitAll()
        )

        // 기본 로그아웃 폼 사용
        .logout(logout -> logout
            .logoutUrl("/logout")           // 로그아웃 요청 URL (POST로 쓰는 게 기본)
            .logoutSuccessUrl("/")          // 로그아웃 성공 후 이동
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID")
        )

        .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**")) // ==> Swagger에서 API를 호출하기 위해서는 CSRF를 꺼야함.
        ;

    return httpSecurity.build();
  }
}
