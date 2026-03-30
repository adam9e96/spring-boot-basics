package com.adam9e96.chapter05jpa.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 게시글(Post) 엔티티 클래스.
 *
 * <p>JPA 엔티티는 데이터베이스 테이블과 1:1로 매핑되는 자바 객체이다.
 * 이 클래스가 {@code @Entity}로 선언되면 JPA가 자동으로 {@code post} 테이블을 생성한다.</p>
 *
 * <h3>사용된 어노테이션 정리</h3>
 * <ul>
 *   <li>{@code @Entity} — 이 클래스가 JPA 엔티티임을 선언 (테이블과 매핑)</li>
 *   <li>{@code @Getter/@Setter} — Lombok이 모든 필드의 getter/setter를 자동 생성</li>
 *   <li>{@code @NoArgsConstructor} — JPA 스펙상 기본 생성자가 필수이므로 Lombok으로 생성</li>
 *   <li>{@code @EntityListeners(AuditingEntityListener.class)} — JPA Auditing 기능 활성화.
 *       {@code @CreatedDate}, {@code @LastModifiedDate} 필드를 자동으로 채워준다.</li>
 * </ul>
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class) // JPA Auditing: 엔티티의 생성/수정 시각을 자동 기록
public class Post {

    /**
     * 기본 키(PK). 데이터베이스가 자동으로 1씩 증가시켜 부여한다.
     * <p>{@code GenerationType.IDENTITY} — DB의 AUTO_INCREMENT(H2/MySQL) 전략 사용</p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 게시글 제목. 빈 값이 들어올 수 없도록 {@code @NotBlank}로 검증한다. */
    @NotBlank
    private String title;

    /** 게시글 본문 내용 */
    @NotBlank
    private String content;

    /** 작성자 이름 */
    @NotBlank
    private String author;

    /**
     * 생성 일시. JPA Auditing이 엔티티가 처음 저장(persist)될 때 자동으로 현재 시각을 설정한다.
     * 한 번 설정되면 이후 변경되지 않는다.
     */
    @CreatedDate
    private LocalDateTime createdAt;

    /**
     * 최종 수정 일시. JPA Auditing이 엔티티가 수정(update)될 때마다 자동으로 갱신한다.
     */
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
