package com.adam9e96.chapter030mvcintro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("hello") // @RequestMapping("hello"): 이 컨트롤러의 기본 URL 패턴을 /hello로 설정
public class HelloModelController {

    @GetMapping("model")
    public String helloView(Model model) {
        // Model 에 데이터를 저장
        model.addAttribute("msg", "타임리프!@!!");
        // 반환값으로 뷰 이름을 돌려줌
        return "helloThymeleaf";

    }
}
