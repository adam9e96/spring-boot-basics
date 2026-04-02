package com.adam9e96.chapter053paginationmodern.controller;

import com.adam9e96.chapter053paginationmodern.entity.Post;
import com.adam9e96.chapter053paginationmodern.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST API 페이지네이션 컨트롤러.
 * <p>
 * Spring Data의 {@link Pageable}을 파라미터로 직접 받으면
 * {@code ?page=0&size=10&sort=id,desc} 쿼리 파라미터가 자동 바인딩된다.
 */
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostApiController {

    private final PostRepository postRepository;

    /**
     * 전체 게시글 페이지네이션 조회.
     * <p>
     * 요청 예시: {@code GET /api/posts?page=0&size=10&sort=id,desc}
     */
    @GetMapping
    public Page<Post> findAll(@PageableDefault(size = 10) Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    /**
     * 키워드 검색 + 페이지네이션.
     * <p>
     * 요청 예시: {@code GET /api/posts/search?keyword=게시글&page=0&size=10}
     */
    @GetMapping("/search")
    public Page<Post> search(
            @RequestParam String keyword,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return postRepository.findByTitleContaining(keyword, pageable);
    }
}
