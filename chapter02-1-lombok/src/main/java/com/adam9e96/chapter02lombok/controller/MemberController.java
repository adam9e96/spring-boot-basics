package com.adam9e96.chapter02lombok.controller;

import com.adam9e96.chapter02lombok.model.Member;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// Lombok으로 생성한 모델 객체가 JSON 응답으로도 자연스럽게 사용되는지 확인하는 예제 컨트롤러다.
@RestController
public class MemberController {

    @GetMapping("/member")
    public Member getMember() {
        return Member.builder()
                .id(1L)
                .name("Spring Boot User")
                .email("user@example.com")
                .age(28)
                .build();
    }
}
