package io.github.minjunbaek.board.domain.member.repository;

import io.github.minjunbaek.board.domain.member.repository.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

  Optional<Member> findByEmail (String email);

  boolean existsByEmail(String email);

  @Query(value = "select case when count(*) > 0 then true else false end " +
      "from members " +
      "where email = :email " +
      "  and deleted_at is not null",
      nativeQuery = true)
  boolean existsDeletedByEmail(@Param(value = "email") String email);
}
