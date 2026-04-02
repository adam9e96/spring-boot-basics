package com.adam9e96.chapter051jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Chapter 05의 시작점이다.
// 이 챕터에서는 JPA + H2 데이터베이스를 사용한 게시판 CRUD API를 구현한다.
@SpringBootApplication
public class Chapter05JpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(Chapter05JpaApplication.class, args);
    }

}
