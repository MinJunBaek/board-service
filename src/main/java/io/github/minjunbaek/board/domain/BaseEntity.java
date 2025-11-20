package io.github.minjunbaek.board.domain;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @CreatedDate // 엔티티 생성시 자동으로 생성 날짜를 넣어줌
  private LocalDateTime createdAt;

  @LastModifiedDate // 엔티티 수정시 자동으로 수정 날짜를 넣어줌
  private LocalDateTime updatedAt;

  private LocalDateTime deletedAt;

  public LocalDateTime deletedAt() {
    this.deletedAt = LocalDateTime.now();
    return deletedAt;
  }

  public boolean isActive() {
    return deletedAt == null; // 상태가 활성중이면 True, 그렇지 않으면 false
  }
}
