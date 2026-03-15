package com.adam9e96.chapter01hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Chapter 01의 시작점이다.
// 별도 컨트롤러 없이도 Spring Boot가 정적 리소스를 서빙하는 흐름을 확인할 수 있다.
@SpringBootApplication
public class Chapter01HelloApplication {

    public static void main(String[] args) {
        SpringApplication.run(Chapter01HelloApplication.class, args);
    }

}
