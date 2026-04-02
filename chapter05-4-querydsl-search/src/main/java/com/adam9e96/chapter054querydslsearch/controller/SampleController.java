package com.adam9e96.chapter054querydslsearch.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@Log4j2
public class SampleController {
    @GetMapping("/hello")
    public void hello(Model model) {
        log.info("Hello World!!!");
        model.addAttribute("msg", "Hello World!!!");
    }

    @GetMapping("/ex/ex1")
    public void ex1(Model model) {
        List<String> list = Arrays.asList("AAA", "BBB", "CCC", "DDD");

        log.info("ex1() ...");

        model.addAttribute("list", list);
    }

    // 내부 클래스
    // SampelDTO를 정의할 때는 반드시 getter 들을 만들어 줘야 함.
    class SampleDTO {
        private String p1, p2, p3;

        public String getP1() {
            return p1;
        }

        public String getP2() {
            return p2;
        }

        public String getP3() {
            return p3;
        }
    }

    @GetMapping("/ex/ex2")
    public void ex2(Model model) {
        log.info("ex/ex2()............");

        // model에 각각 3개의 다른 객체를 넣음
        // 1. list
        List<String> strList = IntStream.range(1, 10)
                .mapToObj(i -> "Data" + i)
                .collect(Collectors.toList());
        model.addAttribute("list", strList);

        // 2. map
        Map<String, String> map = new HashMap<>();
        map.put("A", "AAAA");
        map.put("B", "BBBB");

        model.addAttribute("map", map);

        // 3. 사용자 정의 객체를 생성해서 넘김
        SampleDTO sampleDTO = new SampleDTO();

        sampleDTO.p1 = "Value -- p1";
        sampleDTO.p2 = "Value -- p2";
        sampleDTO.p3 = "Value -- p3";

        model.addAttribute("dto", sampleDTO);
        // ex2.html 추가
    }

    // 레이아웃 예제를 위한 ex3()
    @GetMapping("/ex/ex3")
    public void ex3(Model model) {
        model.addAttribute("arr",new String[] {"AAA","BBB","CCC"});
    }
    // ex3.html 추가


}
