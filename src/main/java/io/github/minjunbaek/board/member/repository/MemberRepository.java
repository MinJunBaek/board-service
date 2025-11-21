package io.github.minjunbaek.board.member.repository;

import io.github.minjunbaek.board.member.repository.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

  Optional<Member> findByEmail (String email);

  boolean existsByEmail(String email);
}
