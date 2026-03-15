package com.adam9e96.chapter03restapibasics.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// 간단한 REST API 실습용 Todo 모델이다.
// JPA 없이 메모리 저장소에서만 사용하므로 도메인 구조를 가볍게 유지한다.
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Todo {

    private Long id;
    private String title;
    private String description;
    private boolean completed;
}
