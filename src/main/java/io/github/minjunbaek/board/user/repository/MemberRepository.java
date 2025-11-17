package io.github.minjunbaek.board.user.repository;

import io.github.minjunbaek.board.user.repository.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

  Optional<Member> findByEmail (String email);

  boolean existsByEmail(String email);
}
