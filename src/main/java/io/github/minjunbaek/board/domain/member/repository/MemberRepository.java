package io.github.minjunbaek.board.domain.member.repository;

import io.github.minjunbaek.board.domain.member.repository.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

  Optional<Member> findByEmail (String email);

  boolean existsByEmail(String email);

  boolean existsByEmailAndDeletedAtIsNotNull(@Param(value = "email") String email);
}
