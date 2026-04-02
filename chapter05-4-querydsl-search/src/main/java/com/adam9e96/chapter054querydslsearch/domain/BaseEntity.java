package com.adam9e96.chapter054querydslsearch.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * BaseEntity 클래스는 다른 엔티티 클래스들이 상속받을 수 있는 기본 클래스입니다.
 * 이 클래스는 모든 상속받은 엔티티들에 대해 자동으로 생성 및 수정 시간을 관리합니다.
 */
@MappedSuperclass // 이 클래스가 다른 엔티티 클래스의 기본 클래스로 사용될 수 있음을 나타냄
@EntityListeners(value = {AuditingEntityListener.class}) // 엔티티 이벤트 처리를 위한 리스너 지정
@Getter // Lombok 라이브러리를 사용하여 모든 필드에 대한 getter 메소드 자동 생성
public class BaseEntity {

    // 데이터가 생성될 때는 둘 다 찍힘.
    @CreatedDate // 엔티티가 생성되어 저장될 때의 시간을 자동으로 관리
    @Column(name = "regDate", updatable = false) // 이 필드의 값을 데이터베이스의 "regDate" 컬럼에 매핑하며, 업데이트는 불가능

    private LocalDateTime regDate; // regDate 필드는 엔티티의 생성 시간을 저장합니다. 이 필드는 엔티티 생성 후 수정되지 않습니다.

    @LastModifiedDate // 엔티티의 최종 수정 시간을 자동으로 관리
    @Column(name = "modDate") // 이 필드의 값을 데이터베이스의 "modDate" 컬럼에 매핑
    private LocalDateTime modDate; //  modDate 필드는 엔티티의 마지막 수정 시간을 저장합니다.
}
