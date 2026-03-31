package com.adam9e96.chapter034requestmapping.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @PathVariable 과 params 를 이용한 요청 매핑 예제 컨트롤러.
 *
 * <ul>
 *     <li>GET /show — 메인 화면</li>
 *     <li>GET /function/{no} — 경로 변수로 분기</li>
 *     <li>POST /send?a|b|c — 같은 폼의 버튼별 분기</li>
 * </ul>
 */
@Controller
@Slf4j
public class PathVariableController {

    /**
     * 메인 화면 표시
     */
    @GetMapping("show")
    public String showView() {
        log.info("showView()");
        return "show";
    }

    /**
     * 경로 변수({no})에 따라 다른 뷰를 반환한다.
     */
    @GetMapping("/function/{no}")
    public String selectFunction(@PathVariable Integer no) {
        log.info("selectFunction(no={})", no);
        String view = switch (no) {
            case 1 -> "pathvariable/function1";
            case 2 -> "pathvariable/function2";
            case 3 -> "pathvariable/function3";
            default -> "show";
        };
        return view;
    }

    /**
     * 버튼 A 클릭 처리 (params = "a")
     */
    @PostMapping(value = "send", params = "a")
    public String showAView() {
        log.info("showAView()");
        return "submit/a";
    }

    /**
     * 버튼 B 클릭 처리 (params = "b")
     */
    @PostMapping(value = "send", params = "b")
    public String showBView() {
        log.info("showBView()");
        return "submit/b";
    }

    /**
     * 버튼 C 클릭 처리 (params = "c")
     */
    @PostMapping(value = "send", params = "c")
    public String showCView() {
        log.info("showCView()");
        return "submit/c";
    }
}
