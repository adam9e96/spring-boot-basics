package com.adam9e96.chapter052paginationbasic.controller;

import com.adam9e96.chapter052paginationbasic.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final PostService postService;

    /**
     * 게시글 목록 페이지.
     *
     * @param page 현재 페이지 번호 (0부터 시작, 기본값 0)
     * @param size 한 페이지당 항목 수 (기본값 10)
     */
    @GetMapping("/")
    public String list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {
        model.addAttribute("posts", postService.findAllPosts(page, size));
        return "posts";
    }
}
