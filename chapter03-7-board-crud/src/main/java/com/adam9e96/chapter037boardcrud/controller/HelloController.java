package com.adam9e96.chapter037boardcrud.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    /**
     * /hello 경로로 GET 요청을 보내면 문자열을 응답하는 간단한 API 엔드포인트 생성
     *
     * @apiNote http://localhost:8080/hello
     *
     * @return "Hello, fpkm999!" 문자열 반환
     */
    @GetMapping("/hello")
    public String hello() {
        return "Hello, fpkm999!";
    }
}
