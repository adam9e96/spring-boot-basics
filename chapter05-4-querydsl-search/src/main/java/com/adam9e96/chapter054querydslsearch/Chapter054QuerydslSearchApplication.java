package com.adam9e96.chapter054querydslsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // JPA 엔티티 객체 생성후 메인 메소드에 추가한 어노테이션

public class Chapter054QuerydslSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(Chapter054QuerydslSearchApplication.class, args);
    }

}
