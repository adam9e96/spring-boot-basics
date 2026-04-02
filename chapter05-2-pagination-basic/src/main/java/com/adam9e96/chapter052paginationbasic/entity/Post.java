package com.adam9e96.chapter052paginationbasic.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 게시글 엔티티.
 * <p>
 * 페이지네이션 학습을 위한 단순한 엔티티로, 대량 데이터를 다루는 상황을 시뮬레이션한다.
 */
@Entity
@Getter
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
