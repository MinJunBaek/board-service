package io.github.minjunbaek.board.user.repository;

import io.github.minjunbaek.board.user.repository.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
