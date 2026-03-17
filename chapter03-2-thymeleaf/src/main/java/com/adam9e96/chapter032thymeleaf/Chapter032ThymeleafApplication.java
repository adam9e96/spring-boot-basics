package com.adam9e96.chapter032thymeleaf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Chapter 03-2: Thymeleaf를 활용한 Todo CRUD 웹 애플리케이션
 * - 서버 사이드 렌더링(SSR) 방식의 웹 UI
 * - 인메모리 저장소 사용
 */
@SpringBootApplication
public class Chapter032ThymeleafApplication {

    public static void main(String[] args) {
        SpringApplication.run(Chapter032ThymeleafApplication.class, args);
    }
}
