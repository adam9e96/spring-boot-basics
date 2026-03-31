package com.adam9e96.chapter02lombok.model;

import lombok.*;

// Lombok 어노테이션으로 반복적인 보일러플레이트 코드를 줄이는 예제 모델이다.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Member {

    private Long id;
    private String name;
    private String email;
    private int age;
}
