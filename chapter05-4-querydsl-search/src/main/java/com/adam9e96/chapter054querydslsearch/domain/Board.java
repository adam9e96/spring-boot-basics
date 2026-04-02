package com.adam9e96.chapter054querydslsearch.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * Board 엔티티 클래스는 게시판 게시글의 정보를 표현합니다.
 * 이 클래스는 데이터베이스의 'board' 테이블과 직접적으로 연결되어 있습니다.
 */
@Entity // 이 클래스를 데이터베이스 테이블과 매핑하기 위해 사용하는 애노테이션
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // GenerationType.IDENTITY는 DB가 알아서 결정하도록함. 마리아DB에선
    // auto Increment가 적용됨 오라클에는 auto Increment가 없다
    private Long bno; // bno 가 primary 키가 된다.

    @Column(length = 500, nullable = false) // 컬럼의 길이와 null 허용 여부
    private String title;

    @Column(length = 2000, nullable = false) // nullable = false : null 불가능
    private String content;

    @Column(length = 50, nullable = false)
    private String writer;

    // 작성일이 빠져있다 -> BaseEntity.java
    // 이 클래스는 BaseEntity를 상속받아야 합니다. BaseEntity 클래스에는 게시글의 생성 및 수정 날짜를 자동으로 처리하는
    // @CreatedDate와 @LastModifiedDate 애노테이션이 포함되어 있습니다. 이를 통해 게시글의 작성일과 수정일이 자동으로 관리됩니다.

    // update 메서드를 위해 추가 메서드
    public void change(String title, String content) {
        this.title = title;
        this.content = content;
    }
    // Getter 만 있었는데 update 를 위해서 setter 기능을 추가함.

}
