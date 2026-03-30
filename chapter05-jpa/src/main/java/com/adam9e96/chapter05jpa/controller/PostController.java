package com.adam9e96.chapter05jpa.controller;

import com.adam9e96.chapter05jpa.dto.PostCreateRequest;
import com.adam9e96.chapter05jpa.dto.PostResponse;
import com.adam9e96.chapter05jpa.dto.PostUpdateRequest;
import com.adam9e96.chapter05jpa.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

/**
 * 게시글 REST 컨트롤러.
 *
 * <p>HTTP 요청을 받아 서비스 계층에 위임하고 응답을 반환한다.</p>
 *
 * <h3>API 엔드포인트 요약</h3>
 * <pre>
 * POST   /posts              → 게시글 생성 (201 Created)
 * GET    /posts               → 게시글 목록 페이징 조회 (200 OK)
 * GET    /posts/{id}          → 게시글 단건 조회 (200 OK)
 * PUT    /posts/{id}          → 게시글 수정 (200 OK)
 * DELETE /posts/{id}          → 게시글 삭제 (204 No Content)
 * GET    /posts/search?author=  → 작성자로 검색 (200 OK)
 * GET    /posts/search?keyword= → 제목 키워드로 검색 (200 OK)
 * </pre>
 *
 * <h3>주요 어노테이션</h3>
 * <ul>
 *   <li>{@code @RestController} — {@code @Controller + @ResponseBody}. 반환 객체가 자동으로 JSON 직렬화된다.</li>
 *   <li>{@code @RequestMapping("/posts")} — 이 컨트롤러의 모든 엔드포인트에 공통 경로 접두사 적용</li>
 *   <li>{@code @Valid} — 요청 DTO의 Bean Validation 어노테이션({@code @NotBlank} 등)을 실행한다.
 *       유효성 검증 실패 시 {@code MethodArgumentNotValidException} 발생 → GlobalExceptionHandler가 처리</li>
 *   <li>{@code @ResponseStatus} — 성공 시 반환할 HTTP 상태 코드를 지정한다.</li>
 * </ul>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    /**
     * 게시글 생성.
     * <p>{@code @RequestBody}로 JSON → DTO 역직렬화, {@code @Valid}로 유효성 검증을 수행한다.</p>
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // 201
    public PostResponse createPost(@Valid @RequestBody PostCreateRequest request) {
        return postService.create(request);
    }

    /**
     * 게시글 목록 페이징 조회.
     * <p>Spring Data의 {@code Pageable}이 쿼리 파라미터를 자동으로 바인딩한다.</p>
     * <p>예: {@code GET /posts?page=0&size=10&sort=createdAt,desc}</p>
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<PostResponse> readPost(Pageable pageable) {
        return postService.findAll(pageable);
    }

    /** 게시글 단건 조회. {@code @PathVariable}로 URL 경로의 {id} 값을 바인딩한다. */
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public PostResponse getPost(@PathVariable Long id) {
        return postService.findById(id);
    }

    /** 게시글 수정. 제목과 내용만 변경 가능하며, 작성자는 변경되지 않는다. */
    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public PostResponse updatePost(@PathVariable Long id, @Valid @RequestBody PostUpdateRequest request) {
        return postService.update(id, request);
    }

    /** 게시글 삭제. 성공 시 본문 없이 204 No Content를 반환한다. */
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204
    public void deletePost(@PathVariable Long id) {
        postService.delete(id);
    }

    /**
     * 게시글 검색. author 또는 keyword 쿼리 파라미터로 검색한다.
     * <p>{@code @RequestParam(required = false)} — 파라미터가 없어도 에러가 발생하지 않는다.</p>
     * <ul>
     *   <li>{@code GET /posts/search?author=홍길동} → 작성자로 검색</li>
     *   <li>{@code GET /posts/search?keyword=JPA} → 제목 키워드로 검색</li>
     * </ul>
     */
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<PostResponse> search(
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String keyword) {

        if (author != null) {
            return postService.findByAuthor(author);
        }

        if (keyword != null) {
            return postService.searchByTitle(keyword);
        }

        // 파라미터가 없으면 빈 리스트 반환
        return List.of();
    }


}
