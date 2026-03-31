package com.adam9e96.chapter04validation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Chapter 04의 시작점이다.
// 이 챕터에서는 Bean Validation을 활용한 회원가입 API를 구현하며
// 입력값 검증 패턴과 에러 응답 처리를 연습한다.
@SpringBootApplication
public class Chapter04ValidationApplication {

    public static void main(String[] args) {
        SpringApplication.run(Chapter04ValidationApplication.class, args);
    }

}
