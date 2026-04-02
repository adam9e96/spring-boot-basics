package com.adam9e96.chapter053paginationmodern.controller;

import com.adam9e96.chapter053paginationmodern.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Thymeleaf 기반 게시글 목록 페이지 컨트롤러.
 * <p>
 * {@link PageRequest}를 사용하여 페이지 번호, 크기, 정렬을 한번에 지정한다.
 */
@Controller
@RequiredArgsConstructor
public class PageController {

    private final PostRepository postRepository;

    @GetMapping("/")
    public String list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction,
            Model model
    ) {
        Sort sortOrder = direction.equalsIgnoreCase("desc")
                ? Sort.by(sort).descending()
                : Sort.by(sort).ascending();

        model.addAttribute("posts", postRepository.findAll(PageRequest.of(page, size, sortOrder)));
        model.addAttribute("currentSort", sort);
        model.addAttribute("currentDirection", direction);
        return "posts";
    }
}
